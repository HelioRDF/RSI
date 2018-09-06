package br.com.rsi.dao.complementos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.domain.complementos.Automacao_Analise_Codigo;
import br.com.rsi.util.HibernateUtil;

/**
 * 
 * [ Detalhes... ] Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 * @author helio.franca
 * @version v1.8
 * @since 24-08-2018
 * 
 */

public class AnaliseCodigoDevDAO extends GenericDAO<Automacao_Analise_Codigo> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -126164611692514074L;

	/**
	 * 
	 * @return - Retorna uma lista de Automacao_Analise_Codigo, aonde a nota é nula
	 */
	@SuppressWarnings("unchecked")
	public List<Automacao_Analise_Codigo> listaNotaVazio() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Automacao_Analise_Codigo.class);
			consulta.add(Restrictions.isNull("notaProjeto"));
			consulta.addOrder(Order.desc("id"));
			List<Automacao_Analise_Codigo> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	//-----------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista de Automacao_Analise_Codigo
	 */
	@SuppressWarnings("unchecked")
	public List<Automacao_Analise_Codigo> listaResultadoVazio() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Automacao_Analise_Codigo.class);
			consulta.add(Restrictions.isNull("resultado"));
			consulta.addOrder(Order.desc("id"));
			List<Automacao_Analise_Codigo> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	//-----------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista de Automacao_Analise_Codigo
	 */
	@SuppressWarnings("unchecked")
	public List<Automacao_Analise_Codigo> listaDebitoTecnico() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Automacao_Analise_Codigo.class);
			consulta.add(Restrictions.isNull("debitoTecnicoMinutos"));
			consulta.addOrder(Order.desc("id"));
			List<Automacao_Analise_Codigo> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	//-----------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista de Automacao_Analise_Codigo
	 */
	@SuppressWarnings("unchecked")
	public List<Automacao_Analise_Codigo> listaCoeficiente() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Automacao_Analise_Codigo.class);
			consulta.add(Restrictions.isNull("coeficiente"));
			consulta.addOrder(Order.desc("id"));
			List<Automacao_Analise_Codigo> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	//-----------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista de Automacao_Analise_Codigo
	 */
	@SuppressWarnings("unchecked")
	public List<Automacao_Analise_Codigo> listaTipoVazio() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Automacao_Analise_Codigo.class);
			consulta.add(Restrictions.isNull("codigoAlterado"));
			consulta.addOrder(Order.desc("id"));
			List<Automacao_Analise_Codigo> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	//-----------------------------------------------------------------------------------
	/**
	 * 
	 * @param codigo
	 *            - int
	 * @param sigla
	 *            - String
	 * @param projeto
	 *            - String
	 * @return - Retorna uma objeto AnaliseCodigoHG
	 */
	public Automacao_Analise_Codigo buscarAnterior(int codigo, String sigla, String projeto) {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Automacao_Analise_Codigo.class);
			// consulta.add(Restrictions.idEq(codigo)); // Realiza uma consulta baseada no
			// ID.
			consulta.add(Restrictions.lt("id", codigo));
			consulta.add(Restrictions.eq("sigla", sigla));
			consulta.add(Restrictions.eq("nomeProjeto", projeto));
			consulta.setMaxResults(1);
			consulta.addOrder(Order.desc("id"));
			Automacao_Analise_Codigo resultado = (Automacao_Analise_Codigo) consulta.uniqueResult(); // Utilizado para
																										// retornar um
																										// unico
			// resultado
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	//-----------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista AnaliseCodigoHK com dataCommit = Null
	 */
	@SuppressWarnings("unchecked")
	public List<Automacao_Analise_Codigo> listarParaDataCommit() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Automacao_Analise_Codigo.class);
			consulta.add(Restrictions.isNull("dataCommit"));
			List<Automacao_Analise_Codigo> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	//-----------------------------------------------------------------------------------
	/**
	 * 
	 * @param codigo
	 *            - int
	 * @param sigla
	 *            - string
	 * @param projeto
	 *            - string
	 * @return - Retorna a quantidade na lista
	 */
	public int qtdList(int codigo, String sigla, String projeto) {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Automacao_Analise_Codigo.class);
			consulta.add(Restrictions.lt("id", codigo));
			consulta.add(Restrictions.eq("sigla", sigla));
			consulta.add(Restrictions.eq("nomeProjeto", projeto));
			@SuppressWarnings("unchecked")
			List<Automacao_Analise_Codigo> resultado = consulta.list();
			return resultado.size();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

}