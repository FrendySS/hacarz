package org.example.hacarz.repository;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.Rating;
import org.example.hacarz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Rating findByUserAndCar(User user, Car car);
    List<Rating> findByCar(Car car);
    void deleteAllByCar(Car car);
    boolean existsByCar(Car car);
}
