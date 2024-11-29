package org.example.hacarz.service;

import org.example.hacarz.entity.Car;
import org.example.hacarz.entity.Comment;
import org.example.hacarz.entity.DTO.CommentDTO;
import org.example.hacarz.entity.User;
import org.example.hacarz.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentRepository commentRepository;

    public Map<String, String> validateCommentData(String commentText) { //Проверка данных коммента на пустоту, совпадение, допустимые символы и т.д. Ошибок нет = сохранение в бд, есть = отправка обратно map ошибок.
        Map<String, String> errors = new HashMap<>();
        if (commentText.isEmpty()) {
            errors.put("commentText", "Текст комментария пуст");
        }
        return errors;
    }

    public Comment addComment(User user_id, Car car, String commentText) { //Сохранение комментария в бд
        Comment comment = new Comment();
        comment.setUser(user_id);
        comment.setCar(car);
        comment.setComment(commentText);
        return commentRepository.save(comment);
    }
    public List<CommentDTO> getCommentsByCarId(int carId) { //Поиск комментариев по айди, перевод их в DTO
        List<Comment> comments = commentRepository.findByCarId(carId);
        List<CommentDTO> commentDTOs = comments.stream()
                .map(comment -> new CommentDTO(comment.getUser().getLogin(), comment.getComment()))
                .collect(Collectors.toList());
        logger.info("Loaded comments: {}", commentDTOs);
        return commentDTOs;
    }
}
