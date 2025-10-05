import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDAOImplTest {

    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("userdb")
            .withUsername("postgres")
            .withPassword("");

    private UserDAOImpl userDAO;

    @BeforeAll
    void startContainer() {
        postgres.start();

        System.setProperty("DB_URL", postgres.getJdbcUrl());
        System.setProperty("DB_USERNAME", postgres.getUsername());
        System.setProperty("DB_PASSWORD", postgres.getPassword());

        userDAO = new UserDAOImpl();
    }

    @AfterAll
    void stopContainer() {
        postgres.stop();
    }

    @Test
    void testCreateAndReadUser() {
        User user = new User("Anna", "anna@example.com", 27);
        userDAO.create(user);
        User found = userDAO.read(user.getId());
        assertNotNull(found);
        assertEquals("Anna", found.getName());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Leo", "leo@example.com", 31);
        userDAO.create(user);
        user.setAge(32);
        userDAO.update(user);
        User updated = userDAO.read(user.getId());
        assertEquals(32, updated.getAge());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Mia", "mia@example.com", 22);
        userDAO.create(user);
        userDAO.delete(user.getId());
        assertNull(userDAO.read(user.getId()));
    }

    @Test
    void testFindAll() {
        userDAO.create(new User("Tom", "tom@example.com", 20));
        List<User> users = userDAO.findAll();
        assertFalse(users.isEmpty());
    }
}
