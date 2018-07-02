package br.com.rsi.dao.complementos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.domain.complementos.ControleGit;
import br.com.rsi.domain.complementos.ControleGitDev;
import br.com.rsi.util.HibernateUtil;

/**
 * [ Detalhes... ] Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 * 
 */

public class ControleGitDAO extends GenericDAO<ControleGit> {

	// Busca o commit mais recente por sigla, nome do sistema...
	public String buscarCommit(String sigla, String nomeSitema) {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(ControleGit.class);
			consulta.add(Restrictions.eq("sigla", sigla));
			consulta.setMaxResults(1);
			consulta.addOrder(Order.desc("dataCommit"));
			ControleGit resultado = (ControleGit) consulta.uniqueResult(); // Utilizado para retornar um unico
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
	
	//Busca ordenada por alteração
	@SuppressWarnings("unchecked")
	public List<ControleGit> listarOrdenandoAlteracao() {
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(ControleGit.class);
			consulta.addOrder(Order.desc("alteracao"));
			List<ControleGit> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
}