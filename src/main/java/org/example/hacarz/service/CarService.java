package org.example.hacarz.service;

import jakarta.transaction.Transactional;
import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.DeletedCar;
import org.example.hacarz.repository.CarRepository;
import org.example.hacarz.repository.CommentRepository;
import org.example.hacarz.repository.RatingRepository;
import org.example.hacarz.repository.UserListRepository;
import org.example.hacarz.repository.DeletedCarRepository;
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
    private DeletedCarRepository deletedCarRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserListRepository userListRepository;

    @Value("${upload.path}")
    private String uploadPath;

    // Проверка данных автомобиля
    public Map<String, String> validateCarData(
            String type,
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

        if (!type.equals("used") && !type.equals("new")) {
            errors.put("type", "Недопустимый тип автомобиля");
        }
        if (make == null || make.isEmpty()) {
            errors.put("make", "Марка автомобиля не может быть пустой");
        }
        if (model == null || model.isEmpty()) {
            errors.put("model", "Модель автомобиля не может быть пустой");
        }
        if (date == null || date.isEmpty()) {
            errors.put("date", "Дата выпуска не может быть пустой");
        }
        if (mileage < 0) {
            errors.put("mileage", "Пробег не может быть отрицательным");
        }
        if (!fuel.equals("petrol") && !fuel.equals("diesel") && !fuel.equals("gas")) {
            errors.put("fuel", "Недопустимый тип топлива");
        }
        if (enginesize <= 0) {
            errors.put("enginesize", "Недопустимый объем двигателя");
        }
        if (power <= 0) {
            errors.put("power", "Недопустимая мощность двигателя");
        }
        if (!gearbox.equals("manual") && !gearbox.equals("auto") && !gearbox.equals("robot")) {
            errors.put("gearbox", "Недопустимый тип коробки передач");
        }
        if (seats <= 0) {
            errors.put("seats", "Недопустимое количество мест");
        }
        if (doors <= 0) {
            errors.put("doors", "Недопустимое количество дверей");
        }
        if (color == null || color.isEmpty()) {
            errors.put("color", "Цвет автомобиля не может быть пустым");
        }
        if (description == null || description.isEmpty()) {
            errors.put("description", "Описание автомобиля не может быть пустым");
        }
        if (price <= 0) {
            errors.put("price", "Цена автомобиля не может быть отрицательной или нулевой");
        }
        if (avatar == null || avatar.isEmpty()) {
            errors.put("avatar", "Фотография автомобиля обязательна");
        }

        // Если ошибок нет, сохраняем автомобиль
        if (errors.isEmpty()) {
            Car car = new Car();
            car.setType(type);
            car.setMake(make);
            car.setModel(model);
            car.setDate(date);
            car.setMileage(mileage);
            car.setFuel(fuel);
            car.setEnginesize(enginesize);
            car.setPower(power);
            car.setGearbox(gearbox);
            car.setSeats(seats);
            car.setDoors(doors);
            car.setColor(color);
            car.setDescription(description);
            car.setExtras(extras);
            car.setPrice(price);

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uniqueAvatarName = UUID.randomUUID().toString() + "." + avatar.getOriginalFilename();
            avatar.transferTo(new File(uploadPath + "/" + uniqueAvatarName));
            car.setAvatar(uniqueAvatarName);

            carRepository.save(car);
        }

        return errors;
    }

    // Получение всех машин
    public List<Car> getCars() {
        return carRepository.findAll();
    }

    // Получение машин по имени
    public List<Car> getCarsByName(String name) {
        return carRepository.findAllByMake(name);
    }

    // Получение машины по ID
    public Car getCarById(int id) {
        return carRepository.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException("Car with ID " + id + " not found"));
    }

    // Удаление машины
    @Transactional
    public Map<String, String> deleteCar(int id) {
        Map<String, String> errors = new HashMap<>();
        Car car = carRepository.findById((long) id).orElseThrow(() -> new IllegalArgumentException("Car not found"));
        if (!carRepository.existsById((long) id)) {
            errors.put("car", "ID не существует");
        } else {
            DeletedCar deletedCar = new DeletedCar();
            deletedCar.setCar_id(car.getCar_id());
            deletedCar.setAvatar(car.getAvatar());
            deletedCar.setColor(car.getColor());
            deletedCar.setDate(car.getDate());
            deletedCar.setDescription(car.getDescription());
            deletedCar.setDoors(car.getDoors());
            deletedCar.setEnginesize(car.getEnginesize());
            deletedCar.setExtras(car.getExtras());
            deletedCar.setFuel(car.getFuel());
            deletedCar.setGearbox(car.getGearbox());
            deletedCar.setMake(car.getMake());
            deletedCar.setMileage(car.getMileage());
            deletedCar.setModel(car.getModel());
            deletedCar.setPower(car.getPower());
            deletedCar.setPrice(car.getPrice());
            deletedCar.setRating(car.getRating());
            deletedCar.setSeats(car.getSeats());
            deletedCar.setType(car.getType());
            deletedCarRepository.save(deletedCar);

            if (commentRepository.existsByCar(car)) {
                commentRepository.deleteAllByCar(car);
            }
            if (ratingRepository.existsByCar(car)) {
                ratingRepository.deleteAllByCar(car);
            }
            if (userListRepository.existsByCar(car)) {
                userListRepository.deleteAllByCar(car);
            }
            carRepository.deleteById((long) id);
        }
        return errors;
    }

    // Получение списка удаленных автомобилей
    public List<DeletedCar> getDeletedCars() {
        return deletedCarRepository.findAll();
    }
}
