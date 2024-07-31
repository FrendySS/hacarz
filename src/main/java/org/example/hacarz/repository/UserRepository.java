package org.example.hacarz.repository;

import org.example.hacarz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { //Используется для взаимодействия с таблицей user в бд
    boolean existsByEmail(String email); //Проверка на существование пользователя по email
    boolean existsByLogin(String login); //Проверка на существование пользователя по login
    User findByLogin(String login); //Поиск пользователя по login
    Optional<User> findById(Long id); //Поиск пользователя по id
}
