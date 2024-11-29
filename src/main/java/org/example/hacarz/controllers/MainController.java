package org.example.hacarz.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.User;
import org.example.hacarz.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CarService carService;
    @GetMapping("/") //GET запрос на отображение главной страницы, добавляет список машин в модель
    public String getIndexPage(@RequestParam(required = false) String search, Model model){
        List<Car> carsList = new ArrayList<>();
        if(search!=null&&!search.isEmpty())
            carsList = carService.getCarsByName(search);
        else
            carsList=carService.getCars();
        model.addAttribute("carsList", carsList);
        return "index";
    }
    @GetMapping("/blog") //GET запрос на отображение блога
    public String getBlogPage(Model model){
        return "blog";
    }
    @GetMapping("/team") //GET запрос на отображение команды
    public String getTeamPage(Model model){
        return "team";
    }
    @GetMapping("/testimonials") //GET запрос на отображение отзывов
    public String getTestimonialsPage(Model model){
        return "testimonials";
    }
    @GetMapping("/terms") //GET запрос на отображение политики
    public String getTermsPage(Model model){
        return "terms";
    }
    @GetMapping("/about-us") //GET запрос на отображение страницы о нас
    public String getLoginPage(Model model){
        return "about-us";
    }
    @GetMapping("/contact") //GET запрос на отображение страницы контактов, добавляет пользователя из сессии в модель
    public String getContactPage(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "contact";
    }
    @GetMapping("/car-details") //GET запрос на отображение единичной страницы
    public String getCarDetailsPage(Model model){
        return "car-details";
    }
}
