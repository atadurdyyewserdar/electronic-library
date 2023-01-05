package com.company.atadu.elibrary.dto;

import lombok.Data;

import java.util.List;

@Data
public class ElectronicFileWithComments {
    private List<CommentDto> comments;
    private ElectronicFileDto books;
    private RatingDto rating;

    public ElectronicFileWithComments(ElectronicFileDto fileDto, List<CommentDto> comments) {
        this.comments = comments;
        this.books = fileDto;
    }
    public ElectronicFileWithComments(ElectronicFileDto fileDto, List<CommentDto> comments, RatingDto rating) {
        this.comments = comments;
        this.books = fileDto;
        this.rating = rating;
    }
}
