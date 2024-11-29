package org.example.hacarz.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.hacarz.entity.Car;
import org.example.hacarz.service.CarService;
import org.example.hacarz.service.UserService;
import org.example.hacarz.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @GetMapping("/admin") //Get запрос на админ страницу на отображение админ страницы, добавляется пользователь и список всех машин в модель на фронт. Также проверяется роль и авторизация пользователя.
    public String getAdminPage(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<Car> carsList = carService.getCars();
        model.addAttribute("carsList", carsList);
        if (user != null && user.getRole().equals("admin")) {
            model.addAttribute("user", user);
            return "admin";
        } else {
            if(user==null)
                return "redirect:/login";
            else return "redirect:/profile";
        }
    }
    @PostMapping("/admin") //POST запрос на создание нового объекта машины в бд, отправляет данные на сервис, где осуществляется проверка. В зависимости от результата проверки добавляет ошибки или сообщение об успехе в модель.
    public String createCar(@RequestParam String type,
                             @RequestParam String make,
                             @RequestParam String carmodel,
                             @RequestParam String date,
                             @RequestParam int mileage,
                             @RequestParam String fuel,
                             @RequestParam int enginesize,
                             @RequestParam int power,
                             @RequestParam String gearbox,
                             @RequestParam int seats,
                             @RequestParam int doors,
                             @RequestParam String color,
                             @RequestParam String description,
                             @RequestParam String extras,
                             @RequestParam int price,
                             @RequestParam MultipartFile avatar,
                             Model model) throws IOException {
        Map<String, String> errors = carService.validateCarData(type, make, carmodel, date, mileage, fuel, enginesize, power, gearbox, seats, doors, color, description, extras, price, avatar);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "admin";
        }
        model.addAttribute("message", "Машина добавлена");
        return "admin";
    }
    @PostMapping("/admin/delete/{id}") //POST запрос на удаление объекта машины из бд, отправляет на сервис и в зависимости от результата перенаправляет пользователя с соответствующим сообщением.
    public String deleteCar(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes){
        Map<String, String> errors = carService.deleteCar(id);
        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/admin";
        }
        redirectAttributes.addFlashAttribute("message", "Машина удалена");
        return "redirect:/admin";
    }
}
