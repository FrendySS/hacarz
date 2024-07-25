package org.example.hacarz.service;

import jakarta.transaction.Transactional;
import org.example.hacarz.entity.Car;
import org.example.hacarz.repository.CarRepository;
import org.example.hacarz.repository.CommentRepository;
import org.example.hacarz.repository.RatingRepository;
import org.example.hacarz.repository.UserListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserListRepository userListRepository;
    @Value("${upload.path}")
    private String uploadPath;
    public Map<String, String> validateCarData(String type,
                                               String make,
                                               String model,
                                               String date,
                                               int mileage,
                                               String fuel,
                                               int enginesize,
                                               int power,
                                               String gearbox,
                                               int seats,
                                               int doors,
                                               String color,
                                               String description,
                                               String extras,
                                               int price,
                                               MultipartFile avatar) throws IOException {
        Map<String, String> errors = new HashMap<>();
        if(!type.equals("used")&&!type.equals("new")){
            errors.put("type", type);
        }
        if(make.isEmpty()){
            errors.put("make","Марка пустая");
        }
        if(model.isEmpty()){
            errors.put("carmodel","Модель пустая");
        }
        if(date.isEmpty()){
            errors.put("date","Дата пустая");
        }
        if(mileage<=0){
            errors.put("mileage","Недопустимый пробег");
        }
        if(!fuel.equals("petrol")&&!fuel.equals("diesel")&&!fuel.equals("gas")){
            errors.put("fuel", fuel);
        }
        if(enginesize<=0){
            errors.put("enginesize","Недопустимый объем");
        }
        if(power<=0){
            errors.put("power","Недопустимая мощность");
        }
        if(!gearbox.equals("manual")&&!gearbox.equals("auto")&&!fuel.equals("robot")){
            errors.put("gearbox", gearbox);
        }
        if(seats<=0){
            errors.put("seats","Недопустимое количество сидений");
        }
        if(doors<=0){
            errors.put("doors","Недопустимое количество дверей");
        }
        if(price<=0){
            errors.put("doors","Недопустимая цена");
        }
        if(color.isEmpty()){
            errors.put("color","Цвет пустой");
        }
        if(description.isEmpty()){
            errors.put("description","Описание пустое");
        }
        if ((avatar == null) || avatar.getOriginalFilename().isEmpty()) {
            errors.put("avatar", "Аватар пуст");
        }
        if (errors.isEmpty()) {
            Car car = new Car();
            car.setType(type);
            car.setMake(make);
            car.setModel(model);
            car.setDate(date);
            car.setMileage(mileage);
            car.setFuel(fuel);
            car.setColor(color);
            car.setEnginesize(enginesize);
            car.setPower(power);
            car.setGearbox(gearbox);
            car.setSeats(seats);
            car.setDoors(doors);
            car.setColor(color);
            car.setDescription(description);
            car.setExtras(extras);
            car.setPrice(price);
            car.setRating(0);
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String UUIDAvatar = UUID.randomUUID().toString();
            String resultAvatarName = UUIDAvatar+"."+avatar.getOriginalFilename();
            avatar.transferTo(new File(uploadPath+"/"+resultAvatarName));
            car.setAvatar(resultAvatarName);
            carRepository.save(car);
        }
        return errors;
    }
    public List<Car> getCars(){
        return carRepository.findAll();
    }
    public List<Car> getCarsByName(String name){
        return carRepository.findAllByName(name);
    }
    public Car getCarById(long id){
        return carRepository.findById(id).orElseThrow(() ->  new IllegalArgumentException("Car not found"));
    }
    @Transactional
    public Map<String, String> deleteCar(int id) {
        Map<String, String> errors = new HashMap<>();
        Car car = carRepository.findById((long) id).orElseThrow(() ->  new IllegalArgumentException("Car not found"));
        if(!carRepository.existsById((long) id))
            errors.put("car","ID не существует");
        if(commentRepository.existsByCar(car))
            commentRepository.deleteAllByCar(car);
        if(ratingRepository.existsByCar(car))
            ratingRepository.deleteAllByCar(car);
        if(userListRepository.existsByCar(car))
            userListRepository.deleteAllByCar(car);
        if(errors.isEmpty()){
            carRepository.deleteById((long) id);
        }
        return errors;
    }
}
