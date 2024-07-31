package org.example.hacarz.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.hacarz.entity.UserList;
import org.example.hacarz.service.CarService;
import org.example.hacarz.service.FavoriteService;
import org.example.hacarz.service.UserService;
import org.example.hacarz.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private CarService carService;
    @GetMapping("/login") //GET запрос на отображение страницы логина
    public String getLoginPage(Model model){
        return "login";
    }
    @GetMapping("/register") //GET запрос на отображение страницы регистрации
    public String getRegisterPage(Model model){
        return "register";
    }
    @PostMapping("/register") //POST запрос на регистрацию пользователя, отправляет данные на проверку в сервис,
    // в зависимости от результата выводит сообщение в модель или атрибуты перехода.
    public String createUser(@RequestParam String login,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String confirm_password,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        Map<String, String> errors = userService.validateUserData(login, email, password, confirm_password);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "register";
        }
        redirectAttributes.addFlashAttribute("message","Вы успешно зарегистрировались, можете войти.");
        return "redirect:/login";
    }
    @PostMapping("/login") //POST запрос на авторизацию пользователя, отправляет данные на проверку в сервис,
    // в зависимости от результата выводит сообщение в модель или добавляет пользователя в сессию и переносит в профиль.
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        if (userService.authenticate(login, password)) {
            session.setAttribute("user", userService.getUser(login));
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
    @GetMapping("/profile") //GET запрос на отображение страницы профиля, добавляет пользователя,
    // его список любимого в модель
    public String userProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<UserList> favorites = favoriteService.findFavoritesByUserId(user);
            model.addAttribute("user", user);
            model.addAttribute("favorites", favorites);
            return "profile";
        } else {
            return "redirect:/login";
        }
    }
    @PostMapping("/profile") //POST запрос на смену пароля пользователя,
    // отправляет данные в сервис и в зависимости от результата выводит сообщение.
    public String passChange(@RequestParam("oldpassword") String oldpassword,
                             @RequestParam("password") String password,
                             @RequestParam("passwordrepeat") String passwordrepeat,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("user");
        Map<String, String> errors = userService.validatePassword(user, oldpassword, password, passwordrepeat);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "profile";
        }else {
            model.addAttribute("user", user);
            model.addAttribute("message", "Пароль успешно изменен");
            return "profile";
        }
    }
    @PostMapping("profile/deleteFromFavorites/{carId}/{userId}") //POST запрос на удаление из списка избранного,
    // отправляет данные в сервис и в зависимости от результата выводит сообщение.
    public String deleteFromFavorites(@PathVariable("carId")int carId,
                                      @PathVariable("userId")int userId,
                                      RedirectAttributes redirectAttributes){
        Map<String, String> errors = favoriteService.deleteFavorite(userService.getUserById(userId),carService.getCarById(carId));
        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/profile";
        }
        redirectAttributes.addFlashAttribute("message", "Машина удалена");
        return "redirect:/profile";
    }


    @GetMapping("/logout") //GET запрос на удаление пользователя из сессии
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
