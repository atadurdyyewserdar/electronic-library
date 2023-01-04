package com.company.atadu.elibrary.dto;

import com.company.atadu.elibrary.model.Comment;
import com.company.atadu.elibrary.model.Rating;
import lombok.Data;

@Data
public class CommentDto {
    private String userName;
    private String firstName;
    private String lastName;
    private String comment;

    private Long bookId;

    public CommentDto(){
    }
    public CommentDto(Comment comment) {
        this.comment = comment.getComment();
        this.firstName = comment.getFirstName();
        this.lastName = comment.getLastName();
        this.userName = comment.getUserName();
    }

    public CommentDto(String userName, String firstName, String lastName, String comment, Long bookId) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comment = comment;
        this.bookId = bookId;
    }

    public CommentDto(String userName, String firstName, String lastName, String comment) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comment = comment;
    }
}
