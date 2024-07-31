package org.example.hacarz.controllers;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.User;
import org.example.hacarz.service.CarService;
import org.example.hacarz.service.RatingService;
import org.example.hacarz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/car-details/rating")
public class RatingController {


    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private RatingService ratingService;

    @PostMapping("/{carId}/{userId}") //POST запрос на оценку машины пользователем
    public String rateCar(@PathVariable("carId") int carId,
                            @PathVariable("userId") int userId,
                            @RequestParam("rating") int rating) {

        User user = userService.getUserById(userId);
        Car car = carService.getCarById(carId);

        if (user == null || car == null) {
            return "redirect:/car-details/error";
        }

        ratingService.rateCar(user, car, rating);

        return "redirect:/car-details/" +  carId;
    }
}
