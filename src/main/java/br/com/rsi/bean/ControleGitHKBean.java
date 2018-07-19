
package br.com.rsi.bean;

import java.io.BufferedReader;

import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Messages;

import br.com.rsi.alertaGit.GitList;
import br.com.rsi.dao.complementos.ControleGitHKDAO;

import br.com.rsi.domain.complementos.ControleGitHK;
import br.com.rsi.email.EnviarEmail;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * -Classe BEAN ControleGitHKBean.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 *
 */

@ManagedBean
@SessionScoped
public class ControleGitHKBean implements Serializable {
	static final Runtime run = Runtime.getRuntime();
	static Process pro;
	static BufferedReader read;
	private static final long serialVersionUID = 1L;
	private ControleGitHK controle;
	private ControleGitHKDAO dao;
	private List<ControleGitHK> listaControle;
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
		ControleGitHKDAO gitDao = new ControleGitHKDAO();
		String resultado = "";
		List<ControleGitHK> git;
		git = gitDao.listarOrdenandoAlteracao();

		for (ControleGitHK obj : git) {

			GitList list = new GitList();
			resultado += list.alertaGit(obj.getSigla(), obj.getNomeSistema(), obj.getDataCommit(),
					obj.getDataCommitAnt(), obj.isAlteracao());
			System.out.println(resultado);
		}
		email.emailHtml(resultado, "TESTE HK");

	}

	/**
	 * Salva objeto do tipo ControleGitHK
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
	 * Lista os objetos do tipo ControleGitHK
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {
			dao = new ControleGitHKDAO();
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
	 * 
	 */
	// -------------------------------------------------------------------------------------------
	public void salvarPlanilha() {
		controle = new ControleGitHK();
		dao = new ControleGitHKDAO();
		String sigla, sistema, caminho, pacote;
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
				Cell celula3 = sheet.getCell(2, i); // coluna 4 - pacote
				Cell celula4 = sheet.getCell(3, i); // coluna 3 - caminho

				sigla = celula1.getContents().toString().trim().toUpperCase();
				sistema = celula2.getContents().toString().trim().toUpperCase();
				pacote = celula3.getContents().toString().trim().toUpperCase();
				caminho = celula4.getContents().toString().trim().toUpperCase();

				// Encerra a leitura quando encontra linha vazia
				if (sigla.isEmpty()) {
					break;
				}

				if (!sigla.isEmpty()) {
					dateC = new Date();
					controle.setSigla(sigla);
					controle.setNomeSistema(sistema);
					controle.setCaminho(caminho);
					controle.setPacote(pacote);
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
	 * Limpa as informações da tabela ControleGitHK no banco de dados
	 */
	// -------------------------------------------------------------------------------------
	public void limparDB() {
		try {
			listarInfos();
			for (ControleGitHK ControleGitHK : listaControle) {
				ControleGitHK entidade = dao.buscar(ControleGitHK.getCodigo());
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
	 * Chama o Runnable do gitlog
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public static void gerarLogGit() {
		try {
			new Thread(gitLog).start();
			Messages.addGlobalInfo("Git log em execução!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Messages.addGlobalError("Erro ao executar Git Log!");
		}
	}

	/**
	 * Runnable para acionar o comando gitlog e capturar as informações.
	 */
	// -------------------------------------------------------------------------------------
	private static Runnable gitLog = new Runnable() {
		public void run() {
			List<ControleGitHK> listaControle;
			ControleGitHKDAO dao = new ControleGitHKDAO();
			listaControle = dao.listar();

			for (ControleGitHK ControleGitHK : listaControle) {
				ControleGitHK entidade = dao.buscar(ControleGitHK.getCodigo());
				String pathSigla = "cd " + entidade.getCaminho();
				String comandoGit = "git log --stat -1 --date=format:%d/%m/%Y";
				String[] cmds = { pathSigla, comandoGit };
				StringBuilder log = new StringBuilder();
				log.append("\n \n");
				String author = "", dataCommit = "", descricaoLog;
				Date dataVerificacao = new Date();

				try {
					ProcessBuilder builder = new ProcessBuilder("cmd", "/c", String.join("& ", cmds));
					builder.redirectErrorStream(true);
					Process p = builder.start();

					BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					int i = 0;
					while (true) {
						i++;
						line = r.readLine();

						if (i == 2 && line.contains("Author")) {
							author = line;
						}
						if (i == 3 && line.contains("Date")) {
							dataCommit = line;
						}

						if (i == 3 && line.contains("Author")) {
							author = line;
						}
						if (i == 4 && line.contains("Date")) {
							dataCommit = line;
						}

						if (line == null) {
							break;
						}
						log.append(i + ": " + line + "\n");

					}
					author = author.substring(7, author.length()).trim();
					dataCommit = dataCommit.substring(5, dataCommit.length()).trim();
					descricaoLog = log.toString();

					ControleGitHK.setAuthor(author);
					ControleGitHK.setDataCommitAnt(ControleGitHK.getDataCommit());
					ControleGitHK.setDataCommit(ControleGitHKBean.validadorData(dataCommit, "Data Commit"));
					Date dataAtual = ControleGitHK.getDataCommit();
					Date dataAnterior = ControleGitHK.getDataCommitAnt();
					dataAnterior = formatadorData(dataAnterior);

					if (dataAtual.equals(dataAnterior)) {
						ControleGitHK.setAlteracao(false);
					} else {
						ControleGitHK.setAlteracao(true);
					}

					dataVerificacao = new Date();
					ControleGitHK.setDataVerificacao(dataVerificacao);
					ControleGitHK.setDescricaoLog(descricaoLog);

				} catch (Exception e) {
					System.err.println("---------------Erro: -" + e.getMessage());
					author = "----------";
					ControleGitHK.setAuthor(author);
					descricaoLog = "null";
					ControleGitHK.setDescricaoLog(descricaoLog);
				} finally {
					dao.editar(ControleGitHK);
				}
			}

		}
	};

	/**
	 * Método Git Pull que chama uma nova Thread (gitPull)
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void atualizarGit() {

		try {
			new Thread(gitPull).start();
			Messages.addGlobalInfo("Git pull em execução!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Messages.addGlobalError("Erro ao executar Git pull!");
		}
	}

	/**
	 * Metodo para formatar Data
	 * 
	 * @param data
	 *            - recebe uma data
	 * @return - retorna um objeto do tipo Date
	 * @author andre.graca
	 */
	public static Date formatadorData(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		String dataString = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/"
				+ c.get(Calendar.YEAR);
		// System.out.println(dataString);
		return ControleGitHKBean.validadorData(dataString, "Data Anterior");
	}

	/**
	 * Runnable para acionar o gitpull e atualizar os pacotes das aplicações
	 */
	// -------------------------------------------------------------------------------------
	private static Runnable gitPull = new Runnable() {
		public void run() {
			List<ControleGitHK> listaControle;
			ControleGitHKDAO dao = new ControleGitHKDAO();
			listaControle = dao.listar();
			int contasGit = 2;
			alteraLoginGit("xb201520", "pCAV#1212");
			while (contasGit > 0) {
				for (ControleGitHK ControleGitHK : listaControle) {
					ControleGitHK entidade = dao.buscar(ControleGitHK.getCodigo());
					String pathSigla = "cd " + entidade.getCaminho();

					String comandoGit = "git -c http.sslverify=no pull >>LogGit.txt";
					String[] cmds = { pathSigla, comandoGit };
					StringBuilder log = new StringBuilder();
					log.append("\n \n");

					try {
						ProcessBuilder builder = new ProcessBuilder("cmd", "/c", String.join("& ", cmds));
						builder.redirectErrorStream(true);
						Process p = builder.start();

						BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line;
						int i = 0;
						while (true) {
							i++;
							line = r.readLine();
							if (line == null) {
								break;
							}
							log.append(i + ": " + line + "\n");
							System.out.println(line);
						}
						// Messages.addGlobalInfo("Executado com sucesso!");
					} catch (Exception e) {
						// Messages.addGlobalError("Caminho não encontrado ...
						// :\n"
						// +
						// ControleGitHKDev.getNomeSistema());
					} finally {
						dao.editar(ControleGitHK);
					}
				}
				contasGit--;
				alteraLoginGit("XI324337", "elphbbtu");
			}
			gerarLogGit();
		}
	};

	/**
	 * Metodos para escrever no arquivo C:/Users/andre.graca/_netrc Este arquivo
	 * salva o login do GitLab na maquina, o que auxilia no git pull para contas
	 * diferentes.
	 * 
	 * @author andre.graca
	 */
	public static void alteraLoginGit(String login, String senha) {
		PrintStream ps = null;
		try {
			ps = new PrintStream("C:/Users/andre.graca/_netrc");
		} catch (Exception e) {
			System.out.println("Falha ao criar o arquivo C:/Users/andre.graca/_netrc");
		}
		ps.append("machine gitlab.produbanbr.corp\nlogin " + login + "\npassword " + senha);
		ps.close();
	}

	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public ControleGitHK getControle() {
		return controle;
	}

	public void setControle(ControleGitHK controle) {
		this.controle = controle;
	}

	public List<ControleGitHK> getListaControle() {
		return listaControle;
	}

	public void setListaControle(List<ControleGitHK> listaControle) {
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