package database;

import org.barneys.PlayerEntityModel;
import org.barneys.User;
import org.barneys.server.NettyServer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Tworzenie konfiguracji Hibernate
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://" + NettyServer.JDBC_HOST + ":" + NettyServer.JDBC_PORT + "/");
            configuration.setProperty("hibernate.connection.username", NettyServer.HIBERNATE_USERNAME);
            configuration.setProperty("hibernate.connection.password", NettyServer.HIBERNATE_PASSWORD);
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(PlayerEntityModel.class);
            //configuration.configure("hibernate.cfg.xml"); // Ładuje ustawienia z pliku hibernate.cfg.xml

            // Tworzenie fabryki sesji
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            // Obsługa wyjątków podczas inicjalizacji SessionFactory
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Uzyskanie nowej sesji
    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void closeSession() {
        sessionFactory.close();
    }
}
