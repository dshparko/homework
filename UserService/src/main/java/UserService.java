import java.util.List;

public class UserService {
    private final UserDAO userDao;

    public UserService(UserDAO userDao) {
        this.userDao = userDao;
    }

    public void createUser(String name, String email, int age) {
        User user = new User(name, email, age);
        userDao.create(user);
        System.out.println("User was created: " + user);
    }

    public void getUser(long id) {
        User user = userDao.read(id);
        if (user != null) {
            System.out.println("User: " + user);
        } else {
            System.out.println("User wasn't found.");
        }
    }

    public void updateUser(long id, String name, String email, int age) {
        User user = userDao.read(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            userDao.update(user);
            System.out.println("User was updated.");
        } else {
            System.out.println("User wasn't found.");
        }
    }

    public void deleteUser(long id) {
        User user = userDao.read(id);
        userDao.delete(id);
        System.out.println("User with information " + user + " was deleted.");
    }

    public void showAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(System.out::println);
        }
    }
}