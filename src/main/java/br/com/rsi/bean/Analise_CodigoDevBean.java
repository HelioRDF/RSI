package br.com.rsi.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Messages;

import br.com.rsi.dao.complementos.AnaliseCodigoDevDAO;
import br.com.rsi.dao.complementos.ControleGitDevDAO;
import br.com.rsi.dao.complementos.ControleRtcDevDAO;
import br.com.rsi.domain.complementos.Automacao_Analise_Codigo;

/**
 * -Classe BEAN AnaliseCodigoHGBean.
 * 
 * @author helio.franca
 * @version v1.8
 * @since 12-07-2018
 *
 */

@ManagedBean
@SessionScoped
public class Analise_CodigoDevBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Automacao_Analise_Codigo analise;
	private AnaliseCodigoDevDAO dao;
	private List<Automacao_Analise_Codigo> listaAnalise;
	List<Automacao_Analise_Codigo> listaResultado;
	private int total;
	String siglaAtual;

	/**
	 * Salvar um objeto AnaliseCodigoHGBean
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
	 * Criar uma lista com os objetos AnaliseCodigoHGBean
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void listarInfos() {
		try {

			dao = new AnaliseCodigoDevDAO();
			List<Automacao_Analise_Codigo> listaAnaliseTemp = dao.listar();
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
	 * Captura a última data de Commit e tipo em controle git/rtc e carimba na
	 * analíse.
	 */

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void DataCommit() {
		try {

			dao = new AnaliseCodigoDevDAO();
			List<Automacao_Analise_Codigo> listaAnaliseTemp = dao.listarParaDataCommit();

			for (Automacao_Analise_Codigo obj : listaAnaliseTemp) {
				ControleGitDevDAO daoGit = new ControleGitDevDAO();
				ControleRtcDevDAO daoRtc = new ControleRtcDevDAO();
				String dataCommit = daoGit.buscarDataCommit(obj.getSigla().trim()).toString();

				if (!dataCommit.equals("N/A")) {
					dataCommit = dataCommit.substring(0, 11);

				} else {

					dataCommit = daoRtc.buscarDataCommit(obj.getSigla().trim()).toString();
					if (dataCommit.length() > 8) {
						dataCommit = dataCommit.substring(0, 11);
					}

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
	 * Captura as notas anteriores e seta na inspeção
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void infoAnt() {
		try {

			dao = new AnaliseCodigoDevDAO();
			listaResultado = dao.listaResultadoVazio();
			Automacao_Analise_Codigo objAnterior = new Automacao_Analise_Codigo();

			for (Automacao_Analise_Codigo obj : listaResultado) {

				try {
					AnaliseCodigoDevDAO daoTemp = new AnaliseCodigoDevDAO();
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

	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Define o tipo da sigla legado/novo por commit
	 * 
	 */
	public void tipoSigla() {
		String tipo = "NOVO";
		dao = new AnaliseCodigoDevDAO();
		List<Automacao_Analise_Codigo> listaAnaliseTemp = dao.listaTipoVazio();

		listaAnalise = listaAnaliseTemp;
		total = listaAnalise.size();

		for (Automacao_Analise_Codigo obj : listaAnaliseTemp) {

			try {
				Automacao_Analise_Codigo objAnterior = dao.buscarAnterior(obj.getId(), obj.getSigla(),
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
	 */
	public void tipoSiglaNtLi() {
		String tipo = "NOVO";
		dao = new AnaliseCodigoDevDAO();
		List<Automacao_Analise_Codigo> listaAnaliseTemp = dao.listaTipoVazio();

		listaAnalise = listaAnaliseTemp;
		total = listaAnalise.size();

		for (Automacao_Analise_Codigo obj : listaAnaliseTemp) {

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

	/**
	 * Trata a coluna debito técnico, deixando apenas o numeral dia.
	 * 
	 * @author helio.franca
	 * @since 13-08-2018
	 * 
	 */
	public void tratarDebitoTecnico() {

		dao = new AnaliseCodigoDevDAO();
		List<Automacao_Analise_Codigo> listaObj = dao.listaDebitoTecnico();
		// List<Automacao_Analise_Codigo> listaObj = listarCodigoDev();

		for (Automacao_Analise_Codigo obj : listaObj) {

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

	/**
	 * Calcula o coeficiente, utilizando a formula (=[@[Débito Técnico em Dias
	 * ]]/[@[Total Issus]] ).
	 * 
	 * @author helio.franca
	 * @since 13-08-2018
	 * 
	 */
	public void calcularCoeficiente() {

		dao = new AnaliseCodigoDevDAO();
		List<Automacao_Analise_Codigo> listaObj = dao.listaCoeficiente();

		for (Automacao_Analise_Codigo obj : listaObj) {
			double totalIssues = obj.getIssuesMuitoAlta() + obj.getIssuesAlta() + obj.getIssuesMedia()
					+ obj.getIssuesBaixa();
			double debitoDias = (Integer.parseInt(obj.getDebitoTecnicoMinutos()) / 60 / 24);
			double coeficiente = 0;

			if (totalIssues > 0) {
				coeficiente = debitoDias / totalIssues;
			}
			obj.setCoeficiente(Double.toString(coeficiente));
			dao.editar(obj);
		}
	}

	/**
	 * Retorna Uma lista do tipo Automacao_Analise_Codigo
	 * 
	 * @author helio.franca
	 * @since 13-08-2018
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public List<Automacao_Analise_Codigo> listarCodigoDev() {
		try {

			dao = new AnaliseCodigoDevDAO();
			List<Automacao_Analise_Codigo> listaAnaliseTemp = dao.listar();
			listaAnalise = listaAnaliseTemp;
			Messages.addGlobalInfo("Lista Atualizada!");
			return listaAnaliseTemp;

		} catch (Exception e) {
			// TODO: handle exception
			Messages.addGlobalError("Erro ao  Atualizar Lista.");
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx ERRO:" + e.getMessage() + e.getCause());
			return null;
		} finally {
		}
	}

	// Get e Set
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	public int getTotal() {
		return total;
	}

	public Automacao_Analise_Codigo getAnalise() {
		return analise;
	}

	public void setAnalise(Automacao_Analise_Codigo analise) {
		this.analise = analise;
	}

	public List<Automacao_Analise_Codigo> getListaAnalise() {
		return listaAnalise;
	}

	public void setListaAnalise(List<Automacao_Analise_Codigo> listaAnalise) {
		this.listaAnalise = listaAnalise;
	}

	public List<Automacao_Analise_Codigo> getListaResultado() {
		return listaResultado;
	}

	public void setListaResultado(List<Automacao_Analise_Codigo> listaResultado) {
		this.listaResultado = listaResultado;
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