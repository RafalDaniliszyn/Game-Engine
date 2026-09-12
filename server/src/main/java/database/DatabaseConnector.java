package database;

import io.netty.channel.ChannelId;
import org.barneys.PlayerEntityModel;
import org.barneys.User;
import org.barneys.WorldState;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Optional;
import java.util.UUID;

public class DatabaseConnector {


    public static User read(UUID uuid) {
        // Pobranie sesji Hibernate
        Session session = HibernateUtil.getSession();
        User user = null;
        try {
            // Rozpoczęcie transakcji
            session.beginTransaction();

            // Odczyt użytkownika z bazy danych
            user = session.get(User.class, uuid);

            // Wyświetlenie danych użytkownika
            if (user != null) {
                System.out.println(user);
            } else {
                System.out.println("User not found with ID: " + uuid);
            }

            // Commit transakcji
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            // Zamknięcie sesji
            session.close();
        }

        // Zamknięcie fabryki sesji Hibernate
        //HibernateUtil.closeSession();
        return user;
    }

    public static void save(ChannelId channelId) {
        WorldState.channelIdByUserUuidMap.getOrDefault(channelId, Optional.empty()).ifPresent((uuid) -> {
            User user = WorldState.getByUuid(uuid);
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            PlayerEntityModel playerEntityModel = user.getPlayerEntityModel();
            System.out.println(playerEntityModel);
            session.saveOrUpdate(playerEntityModel);
            transaction.commit();
            session.close();
            System.out.println("SAVE_USER");
        });
    }

}
