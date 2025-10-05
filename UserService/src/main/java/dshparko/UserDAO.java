package dshparko;

import java.util.List;
@Deprecated
public interface UserDAO {
    void create(User user);

    User read(Long id);

    void update(User user);

    void delete(Long id);

    List<User> findAll();
}
