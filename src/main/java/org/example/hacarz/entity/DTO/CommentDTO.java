package org.example.hacarz.entity.DTO;

public class CommentDTO { //Используется для вывода комментариев в нужном формате
    private String login;
    private String commentText;

    public CommentDTO(String login, String commentText) {
        this.login = login;
        this.commentText = commentText;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
