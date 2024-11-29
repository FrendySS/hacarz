package org.example.hacarz.service;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.User;
import org.example.hacarz.entity.UserList;
import org.example.hacarz.repository.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {
    @Autowired
    private UserListRepository userListRepository;


    public void addToFavorites(User user, Car car) { //Добавление в избранное
        UserList existingFavorite = userListRepository.findByUserAndCar(user, car);

        if (existingFavorite == null) {
            UserList newUserList = new UserList();
            newUserList.setUser(user);
            newUserList.setCar(car);
            newUserList.setListType("FAVORITES");

            userListRepository.save(newUserList);
        }
    }
    public Map<String, String> deleteFavorite(User user, Car car){ //Удаление из избранного
        Map<String, String> errors = new HashMap<>();
        UserList userList = userListRepository.findByUserAndCar(user, car);
        if(userList==null){
            errors.put("favorite", "Не существует избранного");
        }
        if(errors.isEmpty()){
            userListRepository.delete(userList);
        }
        return errors;
    }

    public List<UserList> findFavoritesByUserId(User user_id) { //Поиск избранного по пользователю
        return userListRepository.findByUserAndListType(user_id, "FAVORITES");
    }
}
