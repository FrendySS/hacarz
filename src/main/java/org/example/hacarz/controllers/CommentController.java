package org.example.hacarz.controllers;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.Comment;
import org.example.hacarz.entity.User;
import org.example.hacarz.service.CarService;
import org.example.hacarz.service.CommentService;
import org.example.hacarz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/car-details/comments")
public class CommentController {
    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @PostMapping("/{carId}/{userId}") //POST запрос на сохранение комментария пользователя к определенной машине
    public String saveComment(@PathVariable("userId") int userId,
                              @PathVariable("carId") int carId,
                              @RequestParam("comment") String commentText,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            Car car = carService.getCarById(carId);
            Map<String, String> errors = commentService.validateCommentData(commentText);
            if (!errors.isEmpty()) {
                model.addAttribute("validationErrors", errors);
                return "redirect:/car-details/" + carId + "?error=validationError";
            }

            Comment createdComment = commentService.addComment(user, car, commentText);
            if (createdComment != null) {
                return "redirect:/car-details/" + carId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Не удалось добавить комментарий");
                return "redirect:/car-details/" + carId + "?error=commentNotAdded";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Произошла непредвиденная ошибка");
            return "redirect:/car-details/" + carId + "?error=unexpectedError";
        }
    }
}
