package br.com.rsi.dao.complementos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.domain.complementos.AnaliseCodigoHK;
import br.com.rsi.util.HibernateUtil;

/**
 * 
 * [ Detalhes... ] Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 * 
 */

public class AnaliseCodigoHKDAO extends GenericDAO<AnaliseCodigoHK> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5640293968135258957L;

	// ---------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista de Automacao_Analise_Codigo, aonde a nota é nula
	 */
	@SuppressWarnings("unchecked")
	public List<AnaliseCodigoHK> listaNotaVazio() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
			consulta.add(Restrictions.isNull("notaProjeto"));
			consulta.addOrder(Order.desc("id"));
			List<AnaliseCodigoHK> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	// ---------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista de AnaliseCodigoHK
	 */
	@SuppressWarnings("unchecked")
	public List<AnaliseCodigoHK> listaResultadoVazio() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
			consulta.add(Restrictions.isNull("resultado"));
			consulta.addOrder(Order.desc("id"));
			List<AnaliseCodigoHK> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	// ---------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista de AnaliseCodigoHK
	 */
	@SuppressWarnings("unchecked")
	public List<AnaliseCodigoHK> listaTipoVazio() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
			consulta.add(Restrictions.isNull("codigoAlterado"));
			consulta.addOrder(Order.desc("id"));
			List<AnaliseCodigoHK> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	// ---------------------------------------------------------------------------------
	/**
	 * 
	 * @param codigo
	 *            - int
	 * @param sigla
	 *            - String
	 * @param projeto
	 *            - String
	 * @return - Retorna uma objeto AnaliseCodigoHK
	 */
	public AnaliseCodigoHK buscarAnterior(int codigo, String sigla, String projeto) {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
			// consulta.add(Restrictions.idEq(codigo)); // Realiza uma consulta baseada no
			// ID.
			consulta.add(Restrictions.lt("id", codigo));
			consulta.add(Restrictions.eq("sigla", sigla));
			consulta.add(Restrictions.eq("nomeProjeto", projeto));
			consulta.setMaxResults(1);
			consulta.addOrder(Order.desc("id"));
			AnaliseCodigoHK resultado = (AnaliseCodigoHK) consulta.uniqueResult(); // Utilizado para retornar um unico
																					// resultado
			return resultado;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}
	}

	// ---------------------------------------------------------------------------------
	/**
	 * 
	 * @return - Retorna uma lista AnaliseCodigoHK com dataCommit = Null
	 */
	@SuppressWarnings("unchecked")
	public List<AnaliseCodigoHK> listarParaDataCommit() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
			consulta.add(Restrictions.isNull("dataCommit"));
			List<AnaliseCodigoHK> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	// ---------------------------------------------------------------------------------
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
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
			consulta.add(Restrictions.lt("id", codigo));
			consulta.add(Restrictions.eq("sigla", sigla));
			consulta.add(Restrictions.eq("nomeProjeto", projeto));
			@SuppressWarnings("unchecked")
			List<AnaliseCodigoHK> resultado = consulta.list();
			return resultado.size();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	// ---------------------------------------------------------------------------------
	/**
	 * 
	 * @param codigo
	 *            - Int
	 * @return - Retorna um objeto filtrado por código
	 */

	public AnaliseCodigoHK buscarPorID(int codigo) {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
			consulta.add(Restrictions.idEq(codigo)); // Realiza uma consulta baseada no ID.
			AnaliseCodigoHK resultado = (AnaliseCodigoHK) consulta.uniqueResult(); // Utilizado para retornar um unico
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
	 * @return - Retorna uma lista de AnaliseCodigoHK
	 */
	@SuppressWarnings("unchecked")
	public List<AnaliseCodigoHK> listaDebitoTecnico() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
			consulta.add(Restrictions.isNull("debitoTecnicoMinutos"));
			consulta.addOrder(Order.desc("id"));
			List<AnaliseCodigoHK> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

}