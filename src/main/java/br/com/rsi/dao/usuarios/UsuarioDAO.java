package br.com.rsi.dao.usuarios;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.rsi.dao.complementos.GenericDAO;
import br.com.rsi.domain.usuarios.Usuario;
import br.com.rsi.util.HibernateUtil;

/**
 * -Classe Dao de usuário. [ Detalhes... ] Referencia.
 * http://www.devmedia.com.br/hibernate-api-criteria-realizando-consultas/29627
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 * 
 */
public class UsuarioDAO extends GenericDAO<Usuario> {

	/**
	 * Autentica usuário do sistema
	 * 
	 * @param email - String
	 * @param senha - String
	 * @return - Retorna um objeto do tipo usuário
	 */
	public Usuario autenticar(String email, String senha) {

		// Abre uma sessão com Hibernate
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Usuario.class);
			consulta.add(Restrictions.eq("email", email));
			SimpleHash hash = new SimpleHash("md5", senha);
			consulta.add(Restrictions.eq("senha", hash.toHex()));
			Usuario resultado = (Usuario) consulta.uniqueResult();
			return resultado;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			sessao.close();
		}
	}

	/**
	 * Busca usuário por ID
	 * 
	 * @param id - Long
	 * @return - Retorna um objeto do tipo usuário
	 */
	public Usuario buscarUsuarioId(Long id) {
		// Abre uma sessão com Hibernate
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Usuario.class);
			consulta.add(Restrictions.eq("codigo", id));
			Usuario resultado = (Usuario) consulta.uniqueResult();
			return resultado;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} finally {
			sessao.close();
		}
	}

	/**
	 * Valida email passando email
	 * @param email - String
	 * @return - Retorna um boolean
	 */
	public Boolean validarEmail(String email) {
		Boolean permitir = false;
		// Abre uma sessão com Hibernate
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		Criteria consulta = sessao.createCriteria(Usuario.class);
		consulta.add(Restrictions.eq("email", email));
		Usuario resultado = (Usuario) consulta.uniqueResult();
		if (resultado == null) {
			permitir = true;
		}
		return permitir;
	}
/**
 * Valida Email passando email e id
 * @param email - String
 * @param id - Long
 * @return - Retorna um boolean
 */
	public Boolean validarEmail(String email, Long id) {
		Boolean permitir = false;
		// Abre uma sessão com Hibernate
		Session sessao = HibernateUtil.getFabricadeSessoes().openSession();
		Criteria consulta = sessao.createCriteria(Usuario.class);
		consulta.add(Restrictions.eq("email", email));
		Usuario resultado = (Usuario) consulta.uniqueResult();
		if (resultado == null || id == resultado.getCodigo()) {
			permitir = true;
		}
		return permitir;

	}

}
