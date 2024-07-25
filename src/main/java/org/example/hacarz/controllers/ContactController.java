package org.example.hacarz.controllers;

import org.example.hacarz.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/contact/sendMessage")
    public String handleFormSubmit(@RequestParam String name, @RequestParam String email,
                                   @RequestParam String subject, @RequestParam String message,
                                   RedirectAttributes redirectAttributes) {
        try {
            String to = "tashmit.musaev@gmail.com";
            String emailContent = "Name: " + name + "\nEmail: " + email + "\n\nMessage:\n" + message;
            emailService.sendSimpleEmail(to, subject, emailContent);
            redirectAttributes.addFlashAttribute("message", "Письмо отправлено");
            return "redirect:/contact";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при отправке письма: " + e.getMessage());
            return "redirect:/contact";
        }
    }
}