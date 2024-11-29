package org.example.hacarz.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "userlist")
public class UserList {//Модель списка пользователя, автоматически создается в бд таблица userlist
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userlist_id")
    private int userlistId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
    @Column(name = "listtype")
    private String listType;

    public int getUserlistId() {
        return userlistId;
    }

    public void setUserlistId(int userlistId) {
        this.userlistId = userlistId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }
}
