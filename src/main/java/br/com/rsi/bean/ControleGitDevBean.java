package br.com.rsi.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Messages;

import br.com.rsi.alertaGit.GitList;
import br.com.rsi.dao.complementos.ControleGitDevDAO;
import br.com.rsi.domain.complementos.ControleGitDev;
import br.com.rsi.email.EnviarEmail;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * -Classe BEAN ControleGitDevBean.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 *
 */

@ManagedBean
@SessionScoped
public class ControleGitDevBean implements Serializable {
	static final Runtime run = Runtime.getRuntime();
	static Process pro;
	static BufferedReader read;
	private static final long serialVersionUID = 1L;
	private ControleGitDev controle;
	private ControleGitDevDAO dao;
	private List<ControleGitDev> listaControle;
	private String pathSigla;
	String path;
	private int total;
	static String CAMINHO = "";

	/**
	 * Dispara o envio de E-mail
	 */
	// -------------------------------------------------------------------------------------
	public void enviarEmail() {
		Messages.addGlobalWarn("Teste");
		EnviarEmail email = new EnviarEmail();
		ControleGitDevDAO gitDao = new ControleGitDevDAO();
		String resultado = "";
		List<ControleGitDev> git;
		git = gitDao.listarOrdenandoAlteracao();

		for (ControleGitDev obj : git) {

			GitList list = new GitList();
			resultado += list.alertaGit(obj.getSigla(), obj.getNomeSistema(), obj.getDataCommit(),
					obj.getDataCommitAnt(), obj.isAlteracao());
			System.out.println(resultado);
		}
		email.emailHtml(resultado, "TESTE DEV");

	}

	/**
	 * // Salvar um objeto do tipo ControleGitDev
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
	 * Lista os objetos do tipo ControleGitDev
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {
			dao = new ControleGitDevDAO();
			listaControle = dao.listar();
			total = listaControle.size();
			Messages.addGlobalInfo("Lista Atualizada!");
		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		} finally {
		}
	}

	/**
	 * Captura as informações de uma planilha xls e salva no banco de dados
	 */
	// -------------------------------------------------------------------------------------------
	public void salvarPlanilha() {
		controle = new ControleGitDev();
		dao = new ControleGitDevDAO();
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
	 * Limpa a tabela do banco de dados.
	 */
	// -------------------------------------------------------------------------------------
	public void limparDB() {
		try {
			listarInfos();
			for (ControleGitDev controleGit : listaControle) {
				ControleGitDev entidade = dao.buscar(controleGit.getCodigo());
				dao.excluir(entidade);
			}
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar ");
			System.out.println(e.getCause());
		}
	}

	/**
	 * Valida e formata um objeto do tipo Data.
	 * 
	 * @param dataInfo
	 *            - Data a ser validada
	 * @param msg
	 *            - Mensagem alternativa.
	 * @return - Retorna um objeto do tipo Date.
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
				System.out.println("\n-----------------------------------------Erro em data " + msg);
				e.printStackTrace();
			}
		}
		return dataFinal;
	}

	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public ControleGitDev getControle() {
		return controle;
	}

	public void setControle(ControleGitDev controle) {
		this.controle = controle;
	}

	public List<ControleGitDev> getListaControle() {
		return listaControle;
	}

	public void setListaControle(List<ControleGitDev> listaControle) {
		this.listaControle = listaControle;
	}

	public String getPathSigla() {
		return pathSigla;
	}

	public void setPathSigla(String pathSigla) {
		this.pathSigla = pathSigla;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		CAMINHO = path;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	
}
