package io.ranjeeta.app.repository;

import io.ranjeeta.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
