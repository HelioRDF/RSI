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

import br.com.rsi.dao.complementos.ControleRtcDevDAO;
import br.com.rsi.domain.complementos.ControleRtcDev;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 
 * -Classe BEAN ControleRtcDevBean.
 * 
 * @author helio.franca
 * @version v2.0.5
 * @since 25-07-2018
 *
 *
 */

@ManagedBean
@SessionScoped
public class ControleRtcDevBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private ControleRtcDev controle;
	private ControleRtcDevDAO dao;
	private List<ControleRtcDev> listaControle;

	private int total;
	static String CAMINHO = "";

	/**
	 * Salva objeto do tipo ControleRtcDev
	 */
	// -------------------------------------------------------------------------------------
	public void salvar() {
		try {
			dao.salvar(controle);
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar ");
		}
	}

	/**
	 * Lista os objetos do tipo ControleRtcDev
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {
			dao = new ControleRtcDevDAO();
			listaControle = dao.listar();
			total = listaControle.size();
			Messages.addGlobalInfo("Lista Atualizada!");

		} catch (Exception e) {
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		}
	}

	/**
	 * Captura as informações de uma planilha xls e salva no banco de dados
	 * 
	 */
	// -------------------------------------------------------------------------------------------
	public void salvarPlanilha() {
		controle = new ControleRtcDev();
		dao = new ControleRtcDevDAO();
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
			Messages.addGlobalError("Não foi possível salvar ");
		}
	}

	/**
	 * Limpa as informações da tabela ControleRtcDev no banco de dados
	 */
	// -------------------------------------------------------------------------------------
	public void limparDB() {
		try {
			listarInfos();
			for (ControleRtcDev ControleRtcDev : listaControle) {
				ControleRtcDev entidade = dao.buscar(ControleRtcDev.getCodigo());
				dao.excluir(entidade);
			}
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar ");
		}
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
			Messages.addGlobalError("Erro ao executar Git Log!");
		}
	}

	/**
	 * Runnable para acionar o comando RTClog e capturar as informações.
	 */
	// -------------------------------------------------------------------------------------
	private static Runnable rtcLog = new Runnable() {
		public void run() {
			List<ControleRtcDev> listaControle;
			ControleRtcDevDAO dao = new ControleRtcDevDAO();
			listaControle = dao.listar();

			for (ControleRtcDev obj : listaControle) {
				lerLogRtc(obj);
				try {

				} catch (Exception e) {
					Messages.addGlobalError("Erro");
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
	public static void lerLogRtc(ControleRtcDev obj) {

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

				}
				if (linha == 2) {
					commitTemp = info;
					String array[] = new String[2];
					array = commitTemp.split(":");
					commitTemp = array[1].trim();
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
					if (obj.getDataCommit() == null) {
						System.out.println("\n Data Nula \n");
					} else {
						System.out.println("\n Achou Data  \n");
						obj.setDataCommitAnt(obj.getDataCommit());
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
			}
		}
		return dataFinal;
	}

	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public ControleRtcDev getControle() {
		return controle;
	}

	public void setControle(ControleRtcDev controle) {
		this.controle = controle;
	}

	public List<ControleRtcDev> getListaControle() {
		return listaControle;
	}

	public void setListaControle(List<ControleRtcDev> listaControle) {
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