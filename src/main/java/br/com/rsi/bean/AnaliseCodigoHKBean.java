package br.com.rsi.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.rsi.dao.complementos.AnaliseCodigoHKDAO;
import br.com.rsi.dao.complementos.ControleGitHKDAO;
import br.com.rsi.domain.complementos.AnaliseCodigoHK;

/**
 * -Classe BEAN AnaliseCodigoHKBean.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
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
	 * Selecionar uma linha da tabela AnaliseCodigoHKBean
	 * @param evento - Seleciona um objeto durante o evento.
	 */
	// -------------------------------------------------------------------------------------------
	public void selecionarAnalise(ActionEvent evento) {
		try {
			analise = (AnaliseCodigoHK) evento.getComponent().getAttributes().get("meuSelect");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar: ");
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
			;
			listaAnalise = listaAnaliseTemp;
			total = listaAnalise.size();
			Messages.addGlobalInfo("Lista Atualizada!");

		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx ERRO:" + e.getMessage() + e.getCause());
		} finally {
		}
	}

	/**
	 * Captura a última data de Commit em controle git e carimba na analíse.
	 */

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void DataCommit() {
		try {

			dao = new AnaliseCodigoHKDAO();
			List<AnaliseCodigoHK> listaAnaliseTemp = dao.listarParaDataCommit();

			for (AnaliseCodigoHK obj : listaAnaliseTemp) {
				ControleGitHKDAO daoGit = new ControleGitHKDAO();
				String dataCommit = daoGit.buscarCommit(obj.getSigla().trim(), "N/A").toString();
				if (!dataCommit.equals("N/A")) {
					dataCommit = dataCommit.substring(0, 11);
				}
				obj.setDataCommit(dataCommit);
				dao.editar(obj);
				Messages.addGlobalInfo("Data de Commit atualizada! " + obj.getSigla());
			}

		} catch (Exception e) {
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		} finally {
		}
	}

	/**
	 * Captura Resultados nulos e seta as regras de Alerta/LIBERADO
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void resultadoVazio() {
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
						System.out.println("\n--- Else 1  --- " + analiseCodigoHK.getId());
						analiseCodigoHK.setResultado("LIBERADO");
					}

					dao.editar(analiseCodigoHK); // Salva alteração
					resultado = analiseCodigoHK.getResultado();
					Messages.addGlobalInfo(" Sigla: " + sigla + "-" + resultado);

				} // Fim do For
			}
		} catch (Exception e) {
			System.out.println("XXXxxx ERRO:" + e.getMessage() + e.getCause());
		}
	}

	/**
	 * Calcula a nota da análise
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void calcNota() {
		listarInfos();
		for (AnaliseCodigoHK obj : listaAnalise) {

			if (obj.getNotaProjeto() == null) {
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
				resultado = Integer.parseInt(df.format(nota));
				obj.setNotaProjeto(String.valueOf(resultado));

				dao = new AnaliseCodigoHKDAO();
				dao.editar(obj);
				Messages.addGlobalInfo("Nota incluída:" + obj.getSigla() + " Nota:" + resultado + "%");
				System.out.println("------ " + resultado);
			}
		}

	}

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