package br.com.rsi.dao.complementos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.domain.complementos.ControleGitDev;
import br.com.rsi.util.HibernateUtil;

/**
 * [ Detalhes... ]
 * 
 * -Classe DAO ControleGIT Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 * @author helio.franca
 * @version v1.8
 * @since 13-07-2018
 * 
 */

public class ControleGitDevDAO extends GenericDAO<ControleGitDev> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3955388182778766107L;

	/**
	 * Busca ordenada por alteração
	 * 
	 * @return - Retorna uma lista de ControleGitDev
	 */
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

	/**
	 * Busca o commit mais recente por sigla, nome do sistema...
	 * 
	 * @param sigla
	 *            - String
	 * @param nomeSitema
	 *            - String
	 * @return - Retorna uma String com a Data
	 */
	public String buscarDataCommit(String sigla) {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(ControleGitDev.class);
			consulta.add(Restrictions.eq("sigla", sigla));
			consulta.setMaxResults(1);
			consulta.addOrder(Order.desc("dataCommit"));
			ControleGitDev resultado = (ControleGitDev) consulta.uniqueResult(); // Utilizado para retornar um unico
			// resultado

			System.out.println("-- Achou:" + resultado.getSigla());
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
	 * Busca o commit mais recente por sigla, nome do sistema...
	 * 
	 * @param sigla
	 *            - String
	 * @param nomeSitema
	 *            - String
	 * @return - Retorna uma String com tipo Legado/Novo
	 */
	public String buscarAlteracaoCommit(String sigla) {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(ControleGitDev.class);
			consulta.add(Restrictions.eq("sigla", sigla));
			consulta.setMaxResults(1);
			consulta.addOrder(Order.desc("dataCommit"));
			ControleGitDev resultado = (ControleGitDev) consulta.uniqueResult(); // Utilizado para retornar um unico
			String alteracao = "N/A";

			if (resultado.isAlteracao()) {
				alteracao = "Novo";
			} else {
				alteracao = "Legado";
			}

			System.out.println("-- Achou:" + resultado.getSigla());
			return alteracao;
		} catch (RuntimeException erro) {
			System.out.println("\n --- XXXX --- Objeto não encontrado: " + sigla);
			System.out.println(erro + "\n ---- XXXX ---");
			return "N/A";

		} finally {
			sessao.close();
		}
	}

}