package org.example.hacarz.repository;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.Rating;
import org.example.hacarz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> { //Используется для взаимодействия с таблицей rating в бд
    Rating findByUserAndCar(User user, Car car); //Поиск рейтинга по пользователю и машине
    List<Rating> findByCar(Car car); //Поиск рейтингов по машине
    void deleteAllByCar(Car car); //Удаление рейтинга по машине
    boolean existsByCar(Car car); //Проверка существования рейтинга по машине
}
