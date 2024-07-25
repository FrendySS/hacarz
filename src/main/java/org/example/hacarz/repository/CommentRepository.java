package org.example.hacarz.repository;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.user u JOIN FETCH c.car a WHERE c.car.car_id = :carId")
    List<Comment> findByCarId(int carId);
    @Query("SELECT c FROM Comment c WHERE c.user = :userId")
    List<Comment> findByCar_UserId(int userId);
    void deleteAllByCar(Car car);
    boolean existsByCar(Car car);
}
