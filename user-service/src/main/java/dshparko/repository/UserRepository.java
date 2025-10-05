package dshparko.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<dshparko.model.User, Long> {

}