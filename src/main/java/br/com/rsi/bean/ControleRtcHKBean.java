
package br.com.rsi.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Messages;

import br.com.rsi.dao.complementos.ControleRtcHKDAO;
import br.com.rsi.domain.complementos.ControleRtcHK;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * -Classe BEAN ControleRtcHKBean.
 * 
 * @author helio.franca
 * @version v2.0.5
 * @since 25-07-2018
 *
 */

@ManagedBean
@SessionScoped
public class ControleRtcHKBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private ControleRtcHK controle;
	private ControleRtcHKDAO dao;
	private List<ControleRtcHK> listaControle;

	private int total;
	static String CAMINHO = "";

	/**
	 * Salva objeto do tipo ControleRtcHK
	 */
	// -------------------------------------------------------------------------------------
	public void salvar() {
		try {
			dao.salvar(controle);
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar ");
			System.out.println(e.getCause());
		}
	}

	/**
	 * Lista os objetos do tipo ControleRtcHK
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {
			dao = new ControleRtcHKDAO();
			listaControle = dao.listar();
			total = listaControle.size();

			Messages.addGlobalInfo("Lista Atualizada!");
		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		} 
	}

	/**
	 * Captura as informações de uma planilha xls e salva no banco de dados
	 * 
	 */
	// -------------------------------------------------------------------------------------------
	public void salvarPlanilha() {
		controle = new ControleRtcHK();
		dao = new ControleRtcHKDAO();
		String sigla, sistema, caminho;
		Date dateC = new Date();

		// Carrega a planilha
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(new File(CAMINHO));

			// Seleciona a aba do excel
			Sheet sheet = workbook.getSheet(0);

			// Numero de linhas com dados do xls
			int linhas = sheet.getRows();
			limparDB();
			for (int i = 1; i < linhas; i++) {
				Cell celula1 = sheet.getCell(0, i); // coluna 1 -Sigla
				Cell celula2 = sheet.getCell(1, i); // coluna 2 -Sistema
				Cell celula3 = sheet.getCell(2, i); // coluna 3 - caminho

				sigla = celula1.getContents().toString().trim().toUpperCase();
				sistema = celula2.getContents().toString().trim().toUpperCase();
				caminho = celula3.getContents().toString().trim().toUpperCase();

				// Encerra a leitura quando encontra linha vazia
				if (sigla.isEmpty()) {
					break;
				}

				if (!sigla.isEmpty()) {
					dateC = new Date();
					controle.setSigla(sigla);
					controle.setNomeSistema(sistema);
					controle.setCaminho(caminho);

					controle.setNomeArquivo(CAMINHO);
					controle.setDataVerificacao(dateC);
					salvar();
				}
			}
			Messages.addGlobalInfo("Planilha salva com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			Messages.addGlobalError("Não foi possível salvar ");
		}
	}

	/**
	 * Limpa as informações da tabela ControleRtcHK no banco de dados
	 */
	// -------------------------------------------------------------------------------------
	public void limparDB() {
		try {
			listarInfos();
			for (ControleRtcHK ControleRtcHK : listaControle) {
				ControleRtcHK entidade = dao.buscar(ControleRtcHK.getCodigo());
				dao.excluir(entidade);
			}
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar ");
			System.out.println(e.getCause());
		}
	}

	/**
	 * Valida e converte uma objeto do tipo data
	 * 
	 * @param dataInfo
	 *            - data para validara
	 * @param msg
	 *            - mensagem opcional
	 * @return - retorna um objeto do tipo data
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public static Date validadorData(String dataInfo, String msg) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		Date dataFinal = new Date();
		if (dataInfo.isEmpty()) {
			dataFinal = null;
		} else {
			try {
				dataFinal = (Date) formatter.parse(dataInfo);
			} catch (ParseException e) {
				dataFinal = null;
				System.out.println("\n-----------------------------------------Erro em data" + msg);
				e.printStackTrace();
			}
		}
		return dataFinal;
	}

	/**
	 * Chama o Runnable do Log RTC
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void gerarLogRTC() {
		try {
			new Thread(rtcLog).start();
			Messages.addGlobalInfo("RTC log em execução!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Messages.addGlobalError("Erro ao executar Git Log!");
		}
	}

	/**
	 * Runnable para acionar o comando RTClog e capturar as informações.
	 */
	// -------------------------------------------------------------------------------------
	private static Runnable rtcLog = new Runnable() {
		public void run() {
			List<ControleRtcHK> listaControle;
			ControleRtcHKDAO dao = new ControleRtcHKDAO();
			listaControle = dao.listar();

			for (ControleRtcHK obj : listaControle) {
				lerLogRtc(obj);
				try {

				} catch (Exception e) {
					System.err.println("---------------Erro: -" + e.getStackTrace());
				} finally {
					dao.editar(obj);
				}
			}
		}
	};

	/**
	 * Faz a leitura de um arquivo txt e trata as informações
	 * 
	 * @param path
	 *            - Caminho do arquivo Txt
	 * @param sigla
	 *            - Sigla do Log
	 */
	// -------------------------------------------------------------------------------------
	public static void lerLogRtc(ControleRtcHK obj) {

		String sigla = obj.getSigla();
		String path = obj.getCaminho();
		StringBuilder log = new StringBuilder();

		Date dataAtual = null;
		Date dataAnt = null;
		Date dataVerificacao = new Date();

		path = path + "Log_" + sigla + ".txt";
		File file = new File(path);
		String siglaTemp, commitTemp, dataTemp = "01/01/1900";

		try (FileReader fileReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));) {

			String info = null;
			int linha = 0;
			while ((info = reader.readLine()) != null) {
				linha++;
				log.append("\n");
				log.append(info);

				if (linha == 1) {
					siglaTemp = info;
					String array[] = new String[2];
					array = siglaTemp.split(":");
					siglaTemp = array[1].trim();
					System.out.println("\n ----------------------------- \n");
					System.out.println("- Sigla: " + siglaTemp);

				}
				if (linha == 2) {
					commitTemp = info;
					String array[] = new String[2];
					array = commitTemp.split(":");
					commitTemp = array[1].trim();
					System.out.println("- Commit: " + commitTemp);
					if (commitTemp.equalsIgnoreCase("True")) {
						obj.setAlteracao(true);
					} else {
						obj.setAlteracao(false);
					}

				}
				if (linha == 3) {
					dataTemp = info;

					String array[] = new String[2];
					array = dataTemp.split(":");
					dataTemp = array[1].trim();
					System.out.println("- Data: " + dataTemp);
					System.out.println("\n ----------------------------- \n");
					if (obj.getDataCommit() == null) {
						System.out.println("\n Data Nula \n");
					} else {
						System.out.println("\n Achou Data  \n");
						dataAnt = obj.getDataCommit();

					}

					obj.setDataCommit(validadorData(dataTemp, ""));
					obj.setDataVerificacao(dataVerificacao);
					dataAtual = obj.getDataCommit();
					if (!dataAtual.equals(dataAnt)) {
						obj.setDataCommitAnt(dataAnt);

					}

				}

			}

			info = reader.readLine();
			obj.setDescricaoLog(log.toString());

		} catch (Exception e) {
			System.out.println("xxxxxxxx " + path);
			// TODO: handle exception
		}
	}

	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public ControleRtcHK getControle() {
		return controle;
	}

	public void setControle(ControleRtcHK controle) {
		this.controle = controle;
	}

	public List<ControleRtcHK> getListaControle() {
		return listaControle;
	}

	public void setListaControle(List<ControleRtcHK> listaControle) {
		this.listaControle = listaControle;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public static String getCAMINHO() {
		return CAMINHO;
	}

	public static void setCAMINHO(String cAMINHO) {
		CAMINHO = cAMINHO;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------

}