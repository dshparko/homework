import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        userService.createUser("Darya", "darya@example.com", 30);
        verify(userDao, times(1)).create(any(User.class));
    }

    @Test
    void testGetUserFound() {
        User user = new User("Ivan", "ivan@example.com", 25);
        user.setId(1L);
        when(userDao.read(1L)).thenReturn(user);

        userService.getUser(1L);

        verify(userDao, times(1)).read(1L);
    }

    @Test
    void testGetUserNotFound() {
        when(userDao.read(2L)).thenReturn(null);
        userService.getUser(2L);
        verify(userDao, times(1)).read(2L);
    }

    @Test
    void testUpdateUserFound() {
        User user = new User("Bob", "bob@example.com", 28);
        user.setId(3L);
        when(userDao.read(3L)).thenReturn(user);

        userService.updateUser(3L, "Bobby", "bobby@example.com", 29);

        verify(userDao).update(argThat(u ->
                u.getName().equals("Bobby") &&
                        u.getEmail().equals("bobby@example.com") &&
                        u.getAge() == 29));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userDao.read(4L)).thenReturn(null);
        userService.updateUser(4L, "Ann", "ann@example.com", 22);
        verify(userDao, never()).update(any());
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(5L);
        verify(userDao).delete(5L);
    }

    @Test
    void testShowAllUsersEmpty() {
        when(userDao.findAll()).thenReturn(Collections.emptyList());
        userService.showAllUsers();
        verify(userDao).findAll();
    }

    @Test
    void testShowAllUsers() {
        List<User> users = Arrays.asList(
                new User("Tom", "tom@example.com", 20),
                new User("Jerry", "jerry@example.com", 21)
        );
        when(userDao.findAll()).thenReturn(users);

        userService.showAllUsers();
        verify(userDao).findAll();
    }
}