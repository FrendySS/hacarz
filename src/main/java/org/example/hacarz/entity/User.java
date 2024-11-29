package org.example.hacarz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id; // Уникальный идентификатор

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 4, max = 50, message = "Логин должен содержать от 4 до 50 символов")
    @Column(unique = true)
    private String login; // Уникальный логин

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат Email")
    @Column(unique = true)
    private String email; // Уникальный email

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password; // Пароль пользователя

    private String role; // Роль пользователя (по умолчанию "user")

    private Date reg_date; // Дата регистрации

    // Геттеры и сеттеры
    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }
}
