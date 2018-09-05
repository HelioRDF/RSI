package br.com.rsi.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
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
 * @version v2.1.9
 * @since 24-08-2018
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

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
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

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
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
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx ERRO:" + e.getMessage() + e.getCause());
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
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
			// Chama calcularCoeficiente();
			calcularCoeficiente();
			// Chama alteracaoSigla();
			alteracaoSigla();
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
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
			double totalIssues = (obj.getIssuesMuitoAlta() + obj.getIssuesAlta() + obj.getIssuesMedia() + obj.getIssuesBaixa());
			double debitoDias = 0;
			double coeficiente = 0;
			try {
				int numtemp = (Integer.parseInt(obj.getDebitoTecnicoMinutos()) / 60 / 24);
				debitoDias = numtemp;
				coeficiente = 0;
			} catch (Exception e) {
				// TODO: handle exception
			}

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
		}
	}

	/**
	 * Calcula a nota da análise atual, e seta nota anterior e linhas anterior.
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void calcNotaInfosAnt() {

		dao = new AnaliseCodigoDevDAO();
		List<Automacao_Analise_Codigo> listaAnaliseTemp = dao.listaNotaVazio();
		Automacao_Analise_Codigo objAnterior = new Automacao_Analise_Codigo();

		for (Automacao_Analise_Codigo obj : listaAnaliseTemp) {
			int resultado;
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

			DecimalFormat df = new DecimalFormat("###,###");
			if (soma >= 0) {
				resultado = Integer.parseInt(df.format(nota));

			} else {
				resultado = 0;
			}
			try {
				objAnterior = dao.buscarAnterior(obj.getId(), obj.getSigla(), obj.getNomeProjeto());
				obj.setNotaAnterior(objAnterior.getNotaProjeto());
				obj.setLinhaCodigoAnt(objAnterior.getLinhaCodigo());

			} catch (Exception e) {
				// Objeto anterior não existe.
				System.out.println("----------- Objeto anterior não identificado ----------------");

			}
			obj.setNotaProjeto(String.valueOf(resultado));
			dao = new AnaliseCodigoDevDAO();
			dao.editar(obj);
		}
	}

	/**
	 * Identifica se ocorreu alteração na sigla.
	 * 
	 */
	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	public void alteracaoSigla() {
		String codigoAlterado = "SIM";
		dao = new AnaliseCodigoDevDAO();
		List<Automacao_Analise_Codigo> listaAnaliseTemp = dao.listaTipoVazio();
		String textoDataCommit = "Vazio";
		boolean resultado = false;
		for (Automacao_Analise_Codigo obj : listaAnaliseTemp) {
			try {
				Automacao_Analise_Codigo objAnterior = dao.buscarAnterior(obj.getId(), obj.getSigla(),
						obj.getNomeProjeto());
				textoDataCommit = obj.getDataCommit().toString();
				Date dataCapturaAnterior = objAnterior.getDataCaptura();
				if (textoDataCommit.equalsIgnoreCase("N/A")) {
					codigoAlterado = "LEGADO";
					continue;
				}
				String array[] = new String[3];
				array = textoDataCommit.split("-");
				textoDataCommit = array[0].trim() + "/" + array[1].trim() + "/" + array[2].trim();
				@SuppressWarnings("deprecation")
				Date dataCommit = new Date(textoDataCommit);
				resultado = dataCommit.before(dataCapturaAnterior);
				if (resultado) {
					codigoAlterado = "LEGADO";
				} else {
					codigoAlterado = "NOVO";
				}
				if (!obj.getNotaProjeto().equalsIgnoreCase(objAnterior.getNotaProjeto())) {
					codigoAlterado = "NOVO";
				}
			} catch (Exception e) {
				codigoAlterado = "LEGADO";
			} finally {
				obj.setCodigoAlterado(codigoAlterado);
				dao.editar(obj);
			}
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