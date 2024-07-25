package org.example.hacarz.repository;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.User;
import org.example.hacarz.entity.UserList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserListRepository extends JpaRepository<UserList, Integer> {
        @Query("SELECT ul FROM UserList ul WHERE ul.user = :user AND ul.car = :car")
        UserList findByUserAndCar(@Param("user") User user, @Param("car") Car car);

        @Query("SELECT ul FROM UserList ul WHERE ul.user = :user AND ul.listType = :listType")
        List<UserList> findByUserAndListType(@Param("user") User user, @Param("listType") String listType);
        void deleteAllByCar(Car car);
        boolean existsByCar(Car car);
}

