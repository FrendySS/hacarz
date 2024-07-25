package org.example.hacarz.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.DTO.CommentDTO;
import org.example.hacarz.entity.User;
import org.example.hacarz.service.CarService;
import org.example.hacarz.service.CommentService;
import org.example.hacarz.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarController {
    @Autowired
    private CarService carService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FavoriteService favoriteService;
    @GetMapping("/cars")
    public String getCarsPage(@RequestParam(required = false) String search, Model model){
        List<Car> carsList = new ArrayList<>();
        if(search!=null&&!search.isEmpty())
            carsList = carService.getCarsByName(search);
        else
            carsList=carService.getCars();
        model.addAttribute("carsList", carsList);
        return "cars";
    }

    @GetMapping("/car-details/{id}")
    public String getCarPage(@PathVariable("id") int id, Model model, HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        Car car = carService.getCarById(id);
        List<CommentDTO> comments = commentService.getCommentsByCarId(id);
        model.addAttribute("averageRating", car.getRating());
        model.addAttribute("comments", comments);
        model.addAttribute("user", user);
        model.addAttribute("car", car);
        return "car-details";
    }
    @PostMapping("/car-details/addToFavorites/{carId}")
    public String addToFavorites(@PathVariable("carId") int carId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            Car car = carService.getCarById(carId);
            if (car != null) {
                favoriteService.addToFavorites(user, car);
                return "redirect:/car-details/" + carId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Машина с ID " + carId + " не найдена.");
                return "redirect:/car-details/"+carId;
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Для добавления машины в избранное необходимо войти в систему.");
        }
        return "redirect:/login";
    }
}
