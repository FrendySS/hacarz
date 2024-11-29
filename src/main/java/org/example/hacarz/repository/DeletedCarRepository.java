package org.example.hacarz.repository;

import org.example.hacarz.entity.DeletedCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedCarRepository extends JpaRepository<DeletedCar, Long> {
}
