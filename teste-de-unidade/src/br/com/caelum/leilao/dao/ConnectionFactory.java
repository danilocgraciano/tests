package br.com.caelum.leilao.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConnectionFactory {

	private static EntityManagerFactory factory;

	static {

		Map<String, String> properties = new HashMap<>();

		properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		properties.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/integration_tests");
		properties.put("javax.persistence.jdbc.user", "postgres");
		properties.put("javax.persistence.jdbc.password", "postgres");
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.connection.shutdown", "true");
		properties.put("hibernate.hbm2ddl.auto", "create");
		properties.put("hibernate.show_sql", "true");
//		properties.put("hibernate.format_sql", "true");

		factory = Persistence.createEntityManagerFactory("persistenceUnit", properties);
	}

	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

}
