import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;
@Slf4j
public class UserDAOImpl implements UserDAO {

    private void executeInsideTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Transaction was failed: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void create(User user) {
        executeInsideTransaction(session -> {
            session.persist(user);
            log.info("User was created: " + user);
        });
    }

    @Override
    public User read(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(User.class, id);
        } catch (Exception e) {
            log.error("Error reading user: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void update(User user) {
        executeInsideTransaction(session -> {
            session.merge(user);
            log.info("User was updated: " + user);
        });
    }

    @Override
    public void delete(Long id) {
        executeInsideTransaction(session -> {
            User user = session.find(User.class, id);
            if (user != null) {
                session.remove(user);
                log.info("User was deleted: " + user);
            } else {
                log.warn("User wasn't found: id=" + id);
            }
        });
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            log.error("Error retrieving users: " + e.getMessage());
            throw e;
        }
    }
}
