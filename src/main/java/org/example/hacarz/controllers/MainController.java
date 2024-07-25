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
    @GetMapping("/")
    public String getIndexPage(@RequestParam(required = false) String search, Model model){
        List<Car> carsList = new ArrayList<>();
        if(search!=null&&!search.isEmpty())
            carsList = carService.getCarsByName(search);
        else
            carsList=carService.getCars();
        model.addAttribute("carsList", carsList);
        return "index";
    }
    @GetMapping("/blog")
    public String getBlogPage(Model model){
        return "blog";
    }
    @GetMapping("/team")
    public String getTeamPage(Model model){
        return "team";
    }
    @GetMapping("/testimonials")
    public String getTestimonialsPage(Model model){
        return "testimonials";
    }
    @GetMapping("/terms")
    public String getTermsPage(Model model){
        return "terms";
    }
    @GetMapping("/about-us")
    public String getLoginPage(Model model){
        return "about-us";
    }
    @GetMapping("/contact")
    public String getContactPage(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "contact";
    }
    @GetMapping("/car-details")
    public String getCarDetailsPage(Model model){
        return "car-details";
    }
}
