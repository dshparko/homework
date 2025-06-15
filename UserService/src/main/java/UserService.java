import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class UserService {
    private final UserDAO userDao;

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public void createUser(String name, String email, int age) {
        User user = new User(name, email, age);
        userDao.create(user);
        log.info("User was created: " + user);
    }

    public void getUser(long id) {
        User user = userDao.read(id);
        if (user != null) {
            log.info("User: " + user);
        } else {
            log.warn("User wasn't found.");
        }
    }

    public void updateUser(long id, String name, String email, int age) {
        User user = userDao.read(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            userDao.update(user);
            log.info("User was updated.");
        } else {
            log.warn("User wasn't found.");
        }
    }

    public void deleteUser(long id) {
        userDao.delete(id);
        log.info("User was deleted.");
    }

    public void showAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            log.info("No users found.");
        } else {
            users.forEach(user -> log.info(user.toString()));
        }
    }
}