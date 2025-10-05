import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
@UtilityClass
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure();

            configuration.addAnnotatedClass(User.class);
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            log.error("Failed to create SessionFactory. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

}
