package br.com.rsi.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

	private static final SessionFactory fabricaDeSessoes = criarFabricaDeSessoes();

	private static SessionFactory criarFabricaDeSessoes() {
		try {
			// Cria a SessionFactory para hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure();

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;

		} catch (Throwable ex) {

			System.err.println("Falha ao criar fabrica de sessão." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getFabricadeSessoes() {
		return fabricaDeSessoes;
	}

}