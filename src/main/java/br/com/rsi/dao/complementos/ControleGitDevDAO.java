package br.com.rsi.dao.complementos;

import java.util.List;

import javax.persistence.OrderBy;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.domain.complementos.ControleGitDev;
import br.com.rsi.util.HibernateUtil;

/**
 * [ Detalhes... ] Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 * 
 */


public class ControleGitDevDAO extends GenericDAO<ControleGitDev> {
	//Busca ordenada por alteração
	@SuppressWarnings("unchecked")
	public List<ControleGitDev> listarOrdenandoAlteracao() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(ControleGitDev.class);
			consulta.addOrder(Order.desc("alteracao"));
			List<ControleGitDev> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

}