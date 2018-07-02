package br.com.rsi.dao.complementos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.domain.complementos.AnaliseCodigoHK;
import br.com.rsi.util.HibernateUtil;

/**
 * [ Detalhes... ] Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 */

public class AnaliseCodigoHKDAO extends GenericDAO<AnaliseCodigoHK> {

	@SuppressWarnings("unchecked")
	public List<AnaliseCodigoHK> listaResultadoVazio() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(AnaliseCodigoHK.class);
		//	consulta.add(Restrictions.ne("resultado", "LIBERADO"));
		//	consulta.add(Restrictions.ne("resultado", "BLOQUEADO"));
		//	consulta.add(Restrictions.isNull("resultado"));
		//	consulta.add(Restrictions.eq("sigla", "WPC"));
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

	public AnaliseCodigoHK buscarAnterior(int codigo, String sigla,String projeto) {
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

	public int qtdList(int codigo, String sigla, String projeto ) {
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

}