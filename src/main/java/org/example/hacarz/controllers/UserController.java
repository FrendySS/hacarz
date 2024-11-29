package org.example.hacarz.controllers;

import jakarta.servlet.http.HttpSession;
import org.example.hacarz.entity.User;
import org.example.hacarz.entity.UserList;
import org.example.hacarz.service.CarService;
import org.example.hacarz.service.FavoriteService;
import org.example.hacarz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
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

    /**
     * Отображение страницы входа
     */
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "login";
    }

    /**
     * Обработка входа пользователя
     */
    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        if (userService.authenticate(login, password)) {
            session.setAttribute("user", userService.getUser(login));
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Неверное имя пользователя или пароль");
            return "login";
        }
    }

    /**
     * Отображение страницы регистрации
     */
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register";
    }

    /**
     * Обработка регистрации пользователя
     */
    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             @RequestParam("confirmPassword") String confirmPassword,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        Map<String, String> errors = userService.validateUserData(user.getLogin(), user.getEmail(), user.getPassword(), confirmPassword);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "register";
        }

        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "Вы успешно зарегистрировались!");
        return "redirect:/login";
    }

    /**
     * Отображение страницы профиля
     */
    @GetMapping("/profile")
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

    /**
     * Обработка смены пароля
     */
    @PostMapping("/profile")
    public String passChange(@RequestParam("oldpassword") String oldPassword,
                             @RequestParam("password") String newPassword,
                             @RequestParam("passwordrepeat") String confirmPassword,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("user");
        Map<String, String> errors = userService.validatePassword(user, oldPassword, newPassword, confirmPassword);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "profile";
        }

        model.addAttribute("user", user);
        model.addAttribute("message", "Пароль успешно изменен");
        return "profile";
    }

    /**
     * Удаление машины из избранного
     */
    @PostMapping("/profile/deleteFromFavorites/{carId}/{userId}")
    public String deleteFromFavorites(@PathVariable("carId") int carId,
                                      @PathVariable("userId") int userId,
                                      RedirectAttributes redirectAttributes) {
        Map<String, String> errors = favoriteService.deleteFavorite(userService.getUserById(userId), carService.getCarById(carId));
        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/profile";
        }
        redirectAttributes.addFlashAttribute("message", "Машина удалена из избранного");
        return "redirect:/profile";
    }

    /**
     * Выход из системы
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
