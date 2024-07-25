package org.example.hacarz.repository;

import org.example.hacarz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    User findByLogin(String login);
    Optional<User> findById(Long id);
}
