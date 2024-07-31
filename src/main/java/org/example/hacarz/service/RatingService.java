package org.example.hacarz.service;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.Rating;
import org.example.hacarz.entity.User;
import org.example.hacarz.repository.CarRepository;
import org.example.hacarz.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private CarRepository carRepository;


    public boolean rateCar(User user, Car car, int rating) { //Оценка машины пользователем, сохранение рейтинга
        Rating existingRating = ratingRepository.findByUserAndCar(user, car);

        if (existingRating != null) {
            existingRating.setRating(rating);
        } else {
            existingRating = new Rating();
            existingRating.setUser(user);
            existingRating.setCar(car);
            existingRating.setRating(rating);
        }

        ratingRepository.save(existingRating);

        updateCarRating(car);
        return false;
    }

    private void updateCarRating(Car car) { //Обновление рейтинга самой машины
        double newRating = calculateAverageRating(car);
        car.setRating(newRating);
        carRepository.save(car);
    }

    private double calculateAverageRating(Car car) { //Вычисление среднего рейтинга, используя все рейтинги определенной машины.
        List<Rating> ratings = ratingRepository.findByCar(car);
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }
        return ratings.isEmpty() ? 0 : sum / ratings.size();
    }

}
