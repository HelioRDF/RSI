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
	private AnaliseCodigoEspanhaDAO analiseDao;
	private List<AnaliseCodigoEspanha> listaAnalise;
	private List<AnaliseCodigoEspanha> listaResultado;
	private int total;
	String siglaAtual;

	/**
	 * Salvar um objeto AnaliseCodigoEspanhaBean
	 */
	// -------------------------------------------------------------------------------------
	public void salvar() {
		try {
			analiseDao.salvar(analise);
			Messages.addGlobalInfo(siglaAtual + " - Salva");
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possível salvar a Silga:" + siglaAtual);
		}
	}

	/**
	 * Editar objeto AnaliseCodigoEspanhaBean
	 */
	// -------------------------------------------------------------------------------------------
	public void editar() {
		try {
			analise = new AnaliseCodigoEspanha();
			analiseDao = new AnaliseCodigoEspanhaDAO();
			analiseDao.editar(analise);
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
			Messages.addGlobalError("Erro na seleção: ");
		}
	}

	/**
	 * Criar uma lista com os objetos AnaliseCodigoEspanhaBean
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {

			analiseDao = new AnaliseCodigoEspanhaDAO();
			List<AnaliseCodigoEspanha> listaAnaliseTemp = analiseDao.listar();
			;
			listaAnalise = listaAnaliseTemp;
			total = listaAnalise.size();
			Messages.addGlobalInfo("Lista Atualizada!");

		} catch (Exception e) {
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
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

					analiseDao = new AnaliseCodigoEspanhaDAO();
					analiseDao.editar(obj);
					Messages.addGlobalInfo("Nota incluída:" + obj.getSigla() + " Nota:" + resultado + "%");
					System.out.println("------ " + resultado);
				}
			} catch (Exception e) {
				Messages.addGlobalError("Erro ao calcular ");
			}
		}

	}

	/**
	 * Captura as Notas e Linhas anteriores e seta na inspeção
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void infoAnt() {
		try {

			analiseDao = new AnaliseCodigoEspanhaDAO();
			listaResultado = analiseDao.listaResultadoVazio();
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
			Messages.addGlobalError("Erro Info ");
		}
	}

	/**
	 * Define o tipo da sigla legado/novo
	 * 
	 */
	public void tipoSigla() {
		String tipo = "NOVO";
		analiseDao = new AnaliseCodigoEspanhaDAO();
		List<AnaliseCodigoEspanha> listaAnaliseTemp = analiseDao.listaTipoVazio();

		listaAnalise = listaAnaliseTemp;
		total = listaAnalise.size();

		for (AnaliseCodigoEspanha obj : listaAnaliseTemp) {

			try {
				AnaliseCodigoEspanha objAnterior = analiseDao.buscarAnterior(obj.getId(), obj.getSigla(),
						obj.getNomeProjeto());

				if (obj.getDataCommit().equalsIgnoreCase(objAnterior.getDataCommit())) {
					tipo = "LEGADO";
				} else {
					tipo = "NOVO";
				}

			} catch (Exception e) {
				Messages.addGlobalError("Erro  tipoSigla ");
			}
			obj.setTipo(tipo);
			analiseDao.editar(obj);

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
		analiseDao = new AnaliseCodigoEspanhaDAO();
		List<AnaliseCodigoEspanha> listaAnaliseTemp = analiseDao.listaTipoVazio();

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
				Messages.addGlobalError("Erro tipoSiglaNtLi() ");
			}
			obj.setTipo(tipo);
			analiseDao.editar(obj);

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