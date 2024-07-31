package org.example.hacarz.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class Rating { //Модель рейтинга машины, автоматически создается в бд таблица rating
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rating_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    private int rating;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRating_id() {
        return rating_id;
    }

    public void setRating_id(int rating_id) {
        this.rating_id = rating_id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
