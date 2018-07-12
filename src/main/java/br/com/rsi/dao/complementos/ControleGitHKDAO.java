package br.com.rsi.dao.complementos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.domain.complementos.ControleGitHK;
import br.com.rsi.util.HibernateUtil;

/**
 * 
 * [ Detalhes... ] 
 * 
 * -Classe DAO ControleGitHK
 * Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 * 
 */

public class ControleGitHKDAO extends GenericDAO<ControleGitHK> {
/**
 * Busca o commit mais recente por sigla, nome do sistema...
 * @param sigla - String
 * @param nomeSitema - String
 * @return - Retorna uma String
 */
	public String buscarCommit(String sigla, String nomeSitema) {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(ControleGitHK.class);
			consulta.add(Restrictions.eq("sigla", sigla));
			consulta.setMaxResults(1);
			consulta.addOrder(Order.desc("dataCommit"));
			ControleGitHK resultado = (ControleGitHK) consulta.uniqueResult(); // Utilizado para retornar um unico
								// resultado
			
			System.out.println("-- Achou:"+resultado.getSigla());
			return resultado.getDataCommit().toString();
		} catch (RuntimeException erro) {
			System.out.println("\n --- XXXX --- Objeto não encontrado: " + sigla);
			System.out.println(erro + "\n ---- XXXX ---");
			return "N/A";

		} finally {
			sessao.close();
		}
	}
	
	/**
	 * 	Busca ordenada por alteração
	 * @return - Retorna uma lista de ControleGitHK
	 */

	@SuppressWarnings("unchecked")
	public List<ControleGitHK> listarOrdenandoAlteracao() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(ControleGitHK.class);
			consulta.addOrder(Order.desc("alteracao"));
			List<ControleGitHK> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
}