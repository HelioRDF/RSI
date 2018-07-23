package br.com.rsi.dao.complementos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.domain.complementos.RFC;
import br.com.rsi.util.HibernateUtil;

/**
 * Classe DAO RFC
 * 
 * [ Detalhes... ] Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 * 
 */

public class RFCDAO extends GenericDAO<RFC> {
	
	
	/**
	 * 
	 * @return - Retorna uma lista de objetos
	 */
	@SuppressWarnings("unchecked")
	public List<RFC> listarRfcEscopo() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(RFC.class);
			consulta.add(Restrictions.eq("inspecionar", "SIM"));
			consulta.add(Restrictions.gt("codInspecao", 0));
			List<RFC> resultado = consulta.list();
			return resultado;
			
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

}