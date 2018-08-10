package br.com.rsi.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.rsi.dao.complementos.AnaliseCodigoEspanhaDAO;
import br.com.rsi.domain.complementos.AnaliseCodigoEspanha;

/**
 * -Classe BEAN AnaliseCodigoEspanhaBean.
 * 
 * @author helio.franca
 * @version v1.8
 * @since 13-07-2018
 *
 */

@ManagedBean
@SessionScoped
public class AnaliseCodigoEspanhaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private AnaliseCodigoEspanha analise;
	private AnaliseCodigoEspanhaDAO dao;
	private List<AnaliseCodigoEspanha> listaAnalise;
	List<AnaliseCodigoEspanha> listaResultado;
	private int total;
	String siglaAtual;

	/**
	 * Salvar um objeto AnaliseCodigoEspanhaBean
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
	 * Editar objeto AnaliseCodigoEspanhaBean
	 */
	// -------------------------------------------------------------------------------------------
	public void editar() {
		try {
			analise = new AnaliseCodigoEspanha();
			dao = new AnaliseCodigoEspanhaDAO();
			dao.editar(analise);
			Messages.addGlobalInfo("Editado com sucesso!!!");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar ");
		}
	}

	/**
	 * Selecionar uma linha da tabela AnaliseCodigoEspanhaBean
	 * 
	 * @param evento
	 *            - Seleciona um objeto durante o evento.
	 */
	// -------------------------------------------------------------------------------------------
	public void selecionarAnalise(ActionEvent evento) {
		try {
			analise = (AnaliseCodigoEspanha) evento.getComponent().getAttributes().get("meuSelect");
		} catch (Exception e) {
			Messages.addGlobalError("Erro ao Editar: ");
		}
	}

	/**
	 * Criar uma lista com os objetos AnaliseCodigoEspanhaBean
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {

			dao = new AnaliseCodigoEspanhaDAO();
			List<AnaliseCodigoEspanha> listaAnaliseTemp = dao.listar();
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
	 * Calcula a nota da análise
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void calcNota() {
		listarInfos();
		for (AnaliseCodigoEspanha obj : listaAnalise) {
			System.out.println("ID: " + obj.getId());

			try {

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

					dao = new AnaliseCodigoEspanhaDAO();
					dao.editar(obj);
					Messages.addGlobalInfo("Nota incluída:" + obj.getSigla() + " Nota:" + resultado + "%");
					System.out.println("------ " + resultado);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	/**
	 * Captura as Notas e Linhas anteriores e seta na inspeção
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void infoAnt() {
		try {

			dao = new AnaliseCodigoEspanhaDAO();
			listaResultado = dao.listaResultadoVazio();
			AnaliseCodigoEspanha objAnterior = new AnaliseCodigoEspanha();

			for (AnaliseCodigoEspanha obj : listaResultado) {

				try {
					AnaliseCodigoEspanhaDAO daoTemp = new AnaliseCodigoEspanhaDAO();
					objAnterior = daoTemp.buscarAnterior(obj.getId(), obj.getSigla(), obj.getNomeProjeto());
					obj.setNotaAnterior(objAnterior.getNotaProjeto());
					obj.setLinhaCodigoAnt(objAnterior.getLinhaCodigo());

					daoTemp.editar(obj);
					System.out.println("\n------\nSigla:" + obj.getSigla());
					System.out.println("Nota:" + obj.getNotaProjeto());
					System.out.println("Nota Ant:" + obj.getNotaAnterior());

				} catch (Exception e) {
					System.out.println("------ ERRO:" + e.getMessage() + e.getCause());
				}

			} // Fim do For

		} catch (Exception e) {
			System.out.println("----- ERRO:" + e.getMessage() + e.getCause());
		}
	}

	/**
	 * Define o tipo da sigla legado/novo
	 * 
	 */
	public void tipoSigla() {
		String tipo = "NOVO";
		dao = new AnaliseCodigoEspanhaDAO();
		List<AnaliseCodigoEspanha> listaAnaliseTemp = dao.listaTipoVazio();

		listaAnalise = listaAnaliseTemp;
		total = listaAnalise.size();

		for (AnaliseCodigoEspanha obj : listaAnaliseTemp) {

			try {
				AnaliseCodigoEspanha objAnterior = dao.buscarAnterior(obj.getId(), obj.getSigla(),
						obj.getNomeProjeto());

				if (obj.getDataCommit().equalsIgnoreCase(objAnterior.getDataCommit())) {
					tipo = "LEGADO";
				} else {
					tipo = "NOVO";
				}

			} catch (Exception e) {
				// TODO: Caso não tenha sigla anterior
			}
			obj.setTipo(tipo);
			dao.editar(obj);

		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Define o tipo da sigla legado/novo por Nota ou linha de códgio
	 * 
	 * @author helio.franca
	 * @since 10-08-2018
	 * 
	 */
	public void tipoSiglaNtLi() {
		String tipo = "NOVO";
		dao = new AnaliseCodigoEspanhaDAO();
		List<AnaliseCodigoEspanha> listaAnaliseTemp = dao.listaTipoVazio();

		listaAnalise = listaAnaliseTemp;
		total = listaAnalise.size();

		for (AnaliseCodigoEspanha obj : listaAnaliseTemp) {

			try {

				int linha = obj.getLinhaCodigo();
				int linhaAnt = obj.getLinhaCodigoAnt();
				String nota = obj.getNotaProjeto();
				String notaAnt = obj.getNotaAnterior();

				if (linha != linhaAnt) {
					tipo = "NOVO";

				} else if (!nota.equalsIgnoreCase(notaAnt)) {

					tipo = "NOVO";

				} else {
					tipo = "LEGADO";

				}

			} catch (Exception e) {
				// TODO: Caso não tenha sigla anterior
			}
			obj.setTipo(tipo);
			dao.editar(obj);

		}

	}

	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	public AnaliseCodigoEspanha getAnalise() {
		return analise;
	}

	public void setAnalise(AnaliseCodigoEspanha analise) {
		this.analise = analise;
	}

	public List<AnaliseCodigoEspanha> getListaAnalise() {
		return listaAnalise;
	}

	public void setListaAnalise(List<AnaliseCodigoEspanha> listaAnalise) {
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