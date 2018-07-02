package br.com.rsi.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.rsi.dao.complementos.AnaliseCodigoHKDAO;
import br.com.rsi.dao.complementos.ControleGitDAO;
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

	// Salvar usuário
	// -------------------------------------------------------------------------------------
	public void salvar() {
		try {
			dao.salvar(analise);
			Messages.addGlobalInfo(siglaAtual + " - Salva");
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar a Silga:" + siglaAtual);
			System.out.println("Erro ao salvar --------------------------------------" + siglaAtual + e);
		} finally {
			fechar();
		}
	}

	// Fechar
	// -------------------------------------------------------------------------------------------
	public void fechar() {
		// RequestContext.getCurrentInstance().reset("formFiltro");
		analise = new AnaliseCodigoHK();
		dao = new AnaliseCodigoHKDAO();
	}

	// Editar
	// -------------------------------------------------------------------------------------------
	public void editar() {
		try {
			analise = new AnaliseCodigoHK();
			dao = new AnaliseCodigoHKDAO();
			dao.editar(analise);
			Messages.addGlobalInfo("Editado com sucesso!!!");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar ");
		} finally {
			fechar();
		}
	}

	// Seleciona uma analise da tabela
	// -------------------------------------------------------------------------------------------
	public void selecionarAnalise(ActionEvent evento) {
		try {
			analise = (AnaliseCodigoHK) evento.getComponent().getAttributes().get("meuSelect");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar: ");
		}
	}

	// Listar Infos
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

	// Data commit Git
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void DataCommit() {
		try {

			dao = new AnaliseCodigoHKDAO();
			List<AnaliseCodigoHK> listaAnaliseTemp = dao.listarParaDataCommit();

			for (AnaliseCodigoHK obj : listaAnaliseTemp) {
				ControleGitDAO daoGit = new ControleGitDAO();
				String dataCommit = daoGit.buscarCommit(obj.getSigla().trim(), "N/A").toString();
				if (!dataCommit.equals("N/A")) {
					dataCommit = dataCommit.substring(0, 11);
				}
				obj.setDataCommit(dataCommit);
				// System.out.println("\nSigla = "+obj.getSigla());
				// System.out.println("Data = "+dataCommit);
				dao.editar(obj);
				Messages.addGlobalInfo("Data de Commit atualizada! " + obj.getSigla());
			}

		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
		} finally {
		}
	}

	// Listar Resultado Vazio e seta as regras de Warning/Passed
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
							// System.out.println("-------Maior -----" + analiseCodigoHK.getSigla()
							// +"-"+analiseCodigoHK.getId() +"\nAtual:"
							// + analiseCodigoHK.getNotaProjeto());
							// System.out.println("\nAnterior::" + objAnterior.getNotaProjeto());

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
			// TODO: handle exception
			System.out.println("XXXxxx ERRO:" + e.getMessage() + e.getCause());
		}
	}

	// Get e Set
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
				// double nota=
				// 1-((blocker/linhaCodigo)*10)+((critical/linhaCodigo)*5)+(major/linhaCodigo)+((minor/linhaCodigo)*0.1);
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