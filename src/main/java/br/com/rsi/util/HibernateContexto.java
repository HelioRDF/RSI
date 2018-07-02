package br.com.rsi.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Classe de configuração de Contexto hibernate.
 * 
 * @author helio.franca
 * @version v1.7
 * @since N/A
 * 
 * 
 */
//Criar ou fecha a sessão de acordo com os eventos do tomcat

public class HibernateContexto implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		HibernateUtil.getFabricadeSessoes().close();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		HibernateUtil.getFabricadeSessoes().openSession();
	}
}
