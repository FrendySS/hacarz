package org.example.hacarz.repository;

import org.example.hacarz.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query(value = "SELECT * FROM car a WHERE a.make ILIKE %:make%", nativeQuery = true)
    List<Car> findAllByName(String make);
}
