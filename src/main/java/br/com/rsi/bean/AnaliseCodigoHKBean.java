package br.com.rsi.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Messages;

import br.com.rsi.dao.complementos.AnaliseCodigoHKDAO;
import br.com.rsi.dao.complementos.ControleGitHKDAO;
import br.com.rsi.dao.complementos.ControleRtcHKDAO;
import br.com.rsi.domain.complementos.AnaliseCodigoHK;

/**
 * -Classe BEAN AnaliseCodigoHKBean.
 * 
 * @author helio.franca
 * @version v2.1.8
 * @since 21-08-2018
 *
 */

@ManagedBean
@SessionScoped
public class AnaliseCodigoHKBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private AnaliseCodigoHK analise;
	private AnaliseCodigoHKDAO dao;
	private List<AnaliseCodigoHK> listaAnalise;
	List<AnaliseCodigoHK> listaResultado;
	private int total;
	String siglaAtual;

	/**
	 * Salvar um objeto AnaliseCodigoHKBean
	 */
	// -------------------------------------------------------------------------------------
	public void salvar() {
		try {
			dao.salvar(analise);
			Messages.addGlobalInfo(siglaAtual + " - Salva");
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar a Silga:" + siglaAtual);
			System.out.println("Erro ao salvar --------------------------------------" + siglaAtual + e);
		}
	}

	/**
	 * Editar objeto AnaliseCodigoHKBean
	 */
	// -------------------------------------------------------------------------------------------
	public void editar() {
		try {
			analise = new AnaliseCodigoHK();
			dao = new AnaliseCodigoHKDAO();
			dao.editar(analise);
			Messages.addGlobalInfo("Editado com sucesso!!!");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar ");
		}
	}

	/**
	 * Criar uma lista com os objetos AnaliseCodigoHKBean
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {
			dao = new AnaliseCodigoHKDAO();
			List<AnaliseCodigoHK> listaAnaliseTemp = dao.listar();
			listaAnalise = listaAnaliseTemp;
			total = listaAnalise.size();
			Messages.addGlobalInfo("Lista Atualizada!");
		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
			System.out.println(" ERRO:" + e.getMessage() + e.getCause());
		}
	}

	/**
	 * Captura a última data de Commit e tipo em controle git/rtc e carimba na
	 * analíse.
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void DataCommit() {
		try {
			dao = new AnaliseCodigoHKDAO();
			List<AnaliseCodigoHK> listaAnaliseTemp = dao.listarParaDataCommit();
			for (AnaliseCodigoHK obj : listaAnaliseTemp) {
				ControleGitHKDAO daoGit = new ControleGitHKDAO();
				ControleRtcHKDAO daoRtc = new ControleRtcHKDAO();
				String dataCommitGit = daoGit.buscarDataCommit(obj.getSigla().trim()).toString();
				String dataCommitRtc = daoRtc.buscarDataCommit(obj.getSigla().trim()).toString();
				String dataCommit = "";
				// String dataMaior;
				if (!dataCommitGit.equals("N/A")) {
					dataCommitGit = dataCommitGit.substring(0, 11);
					dataCommit = dataCommitGit;
				}
				if (dataCommitRtc.length() > 8) {
					dataCommitRtc = dataCommitRtc.substring(0, 11);
					dataCommit = dataCommitRtc;
				}
				obj.setDataCommit(dataCommit);
				dao.editar(obj);
				Messages.addGlobalInfo("Data de Commit atualizada! " + obj.getSigla());
			}
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		} finally {
			// Chama o método AlteracaoSigla ...
			alteracaoSigla();
		}
	}

	/**
	 * Captura Resultados nulos e seta as regras de Alerta/LIBERADO + nota anterior
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void resultado() {
		try {
			dao = new AnaliseCodigoHKDAO();
			listaResultado = dao.listaResultadoVazio();
			AnaliseCodigoHK objAnterior = new AnaliseCodigoHK();
			String sigla;
			String resultado;
			if (listaResultado.isEmpty()) {
				Messages.addGlobalInfo("Análises concluídas");
			} else {
				for (AnaliseCodigoHK analiseCodigoHK : listaResultado) {
					try {
						AnaliseCodigoHKDAO daoTemp = new AnaliseCodigoHKDAO();
						sigla = analiseCodigoHK.getSigla();
						int qtdSiglas = daoTemp.qtdList(analiseCodigoHK.getId(), analiseCodigoHK.getSigla(),
								analiseCodigoHK.getNomeProjeto());
						if (qtdSiglas > 0) {
							objAnterior = daoTemp.buscarAnterior(analiseCodigoHK.getId(), analiseCodigoHK.getSigla(),
									analiseCodigoHK.getNomeProjeto());
							int notaAtual = Integer.parseInt(analiseCodigoHK.getNotaProjeto());
							int notaAnterior = Integer.parseInt(objAnterior.getNotaProjeto());
							analiseCodigoHK.setNotaAnterior(Integer.toString(notaAnterior));
							if (notaAtual >= notaAnterior || notaAtual == 100) {
								analiseCodigoHK.setResultado("LIBERADO");
							} else {
								analiseCodigoHK.setResultado("ALERTA");
							}
						} else {
							analiseCodigoHK.setResultado("LIBERADO");
						}
						dao.editar(analiseCodigoHK); // Salva alteração
						resultado = analiseCodigoHK.getResultado();
						Messages.addGlobalInfo(" Sigla: " + sigla + "-" + resultado);
					} catch (Exception e) {
						// TODO: handle exception
					}
				} // Fim do For
			}
		} catch (Exception e) {
			System.out.println("ERRO:" + e.getMessage() + e.getCause());
		}
	}

	/**
	 * Calcula a nota da análise
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void calcNota() {
		dao = new AnaliseCodigoHKDAO();
		List<AnaliseCodigoHK> listaAnaliseTemp = dao.listaNotaVazio();
		for (AnaliseCodigoHK obj : listaAnaliseTemp) {

			double blocker, critical, major, minor;
			int linhaCodigo;
			blocker = obj.getIssuesMuitoAlta();
			critical = obj.getIssuesAlta();
			major = obj.getIssuesMedia();
			minor = obj.getIssuesBaixa();
			linhaCodigo = obj.getLinhaCodigo();
			blocker = ((blocker / linhaCodigo) * 10);
			critical = ((critical / linhaCodigo) * 5);
			major = ((major / linhaCodigo));
			minor = ((minor / linhaCodigo) * 0.1);
			double soma = blocker + critical + major + minor;
			double nota = ((1 - soma) * 100);
			int resultado;
			DecimalFormat df = new DecimalFormat("###,###");
			if (soma >= 0) {
				resultado = Integer.parseInt(df.format(nota));
			} else {
				resultado = 0;
			}
			obj.setNotaProjeto(String.valueOf(resultado));
			dao = new AnaliseCodigoHKDAO();
			dao.editar(obj);
			Messages.addGlobalInfo("Nota incluída:" + obj.getSigla() + " Nota:" + resultado + "%");

		}
		// Chama o método Resultado ...
		resultado();
	}

	/**
	 * Identifica se ocorreu alteração na sigla.
	 * 
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void alteracaoSigla() {
		String codigoAlterado = "SIM";
		dao = new AnaliseCodigoHKDAO();
		List<AnaliseCodigoHK> listaAnaliseTemp = dao.listaTipoVazio();
		String textoDataCommit = "Vazio";
		boolean resultado = false;
		for (AnaliseCodigoHK obj : listaAnaliseTemp) {
			try {
				AnaliseCodigoHK objAnterior = dao.buscarAnterior(obj.getId(), obj.getSigla(), obj.getNomeProjeto());
				textoDataCommit = obj.getDataCommit().toString();
				Date dataCapturaAnterior = objAnterior.getDataCaptura();
				if (textoDataCommit.equalsIgnoreCase("N/A")) {
					codigoAlterado = "NÃO";
					continue;
				}
				String array[] = new String[3];
				array = textoDataCommit.split("-");
				textoDataCommit = array[0].trim() + "/" + array[1].trim() + "/" + array[2].trim();
				@SuppressWarnings("deprecation")
				Date dataCommit = new Date(textoDataCommit);
				resultado = dataCommit.before(dataCapturaAnterior);
				if (resultado) {
					codigoAlterado = "NÃO";
				} else {
					codigoAlterado = "SIM";
				}
				if (!obj.getNotaProjeto().equalsIgnoreCase(objAnterior.getNotaProjeto())) {
					codigoAlterado = "SIM";
				}
			} catch (Exception e) {
				codigoAlterado = "NÃO";
			} finally {
				obj.setCodigoAlterado(codigoAlterado);
				dao.editar(obj);
			}
		}
	}
	
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Trata a coluna debito técnico, deixando apenas o numeral dia.
	 * 
	 * @author helio.franca
	 * @since 13-08-2018
	 * 
	 */
	public void tratarDebitoTecnico() {

		dao = new AnaliseCodigoHKDAO();
		List<AnaliseCodigoHK> listaObj = dao.listaDebitoTecnico();
		// List<Automacao_Analise_Codigo> listaObj = listarCodigoDev();

		for (AnaliseCodigoHK obj : listaObj) {

			if (obj.getDebitoTecnico().contains("d")) {
				String debitoTecnico = obj.getDebitoTecnico();
				String array[] = new String[2];
				array = debitoTecnico.split("d");

				try {
					int debitoTecnicoMinutos = Integer.parseInt(array[0]);
					debitoTecnicoMinutos = debitoTecnicoMinutos * 24 * 60;
					obj.setDebitoTecnicoMinutos(Integer.toString(debitoTecnicoMinutos));
					dao.editar(obj);

				} catch (Exception e) {
					// Erro de conversão String para Interger.
				}

			} else if (obj.getDebitoTecnico().contains("h")) {
				String debitoTecnico = obj.getDebitoTecnico();
				String array[] = new String[2];
				array = debitoTecnico.split("h");

				try {
					int debitoTecnicoMinutos = Integer.parseInt(array[0]);
					debitoTecnicoMinutos = debitoTecnicoMinutos * 60;
					obj.setDebitoTecnicoMinutos(Integer.toString(debitoTecnicoMinutos));

				} catch (Exception e) {
					// Erro de conversão String para Interger.
				}
			} else if (obj.getDebitoTecnico().contains("m")) {
				String debitoTecnico = obj.getDebitoTecnico();
				String array[] = new String[2];
				array = debitoTecnico.split("m");

				try {
					int debitoTecnicoMinutos = Integer.parseInt(array[0]);
					obj.setDebitoTecnicoMinutos(Integer.toString(debitoTecnicoMinutos));

				} catch (Exception e) {
					// Erro de conversão String para Interger.
				}

			} else {
				obj.setDebitoTecnicoMinutos("0");

			}
			dao.editar(obj);
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	public AnaliseCodigoHK getAnalise() {
		return analise;
	}

	public void setAnalise(AnaliseCodigoHK analise) {
		this.analise = analise;
	}

	public List<AnaliseCodigoHK> getListaAnalise() {
		return listaAnalise;
	}

	public void setListaAnalise(List<AnaliseCodigoHK> listaAnalise) {
		this.listaAnalise = listaAnalise;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getSiglaAtual() {
		return siglaAtual;
	}

	public void setSiglaAtual(String siglaAtual) {
		this.siglaAtual = siglaAtual;
	}
}