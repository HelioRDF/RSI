package br.com.rsi.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
