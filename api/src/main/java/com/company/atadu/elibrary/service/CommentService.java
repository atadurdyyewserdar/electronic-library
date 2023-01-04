package com.company.atadu.elibrary.service;

import com.company.atadu.elibrary.dto.CommentDto;
import com.company.atadu.elibrary.model.Comment;
import com.company.atadu.elibrary.model.ElectronicFile;
import com.company.atadu.elibrary.model.Rating;
import com.company.atadu.elibrary.repo.CommentRepo;
import com.company.atadu.elibrary.repo.ElectronicFileRepo;
import com.company.atadu.elibrary.repo.RatingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepo commentRepo;
    private RatingRepo ratingRepo;
    private ElectronicFileRepo electronicFileRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo, ElectronicFileRepo electronicFileRepo, RatingRepo ratingRepo) {
        this.commentRepo = commentRepo;
        this.electronicFileRepo = electronicFileRepo;
        this.ratingRepo = ratingRepo;
    }

    public List<CommentDto> getCommentsOnBook(Long bookId) {
        List<Comment> comments = commentRepo.findCommentsByElectronicFileId(bookId);
        return comments.stream().map(CommentDto::new).collect(Collectors.toList());
    }

    public CommentDto saveComment(CommentDto commentDto, Long bookId) throws FileNotFoundException {
        ElectronicFile electronicFile = electronicFileRepo.findById(bookId)
                .orElseThrow(() -> new FileNotFoundException("Couldn't find file with id:" + bookId));
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        comment.setUserName(commentDto.getUserName());
        comment.setElectronicFile(electronicFile);
        comment.setUserName(commentDto.getUserName());
        comment.setFirstName(commentDto.getFirstName());
        comment.setLastName(commentDto.getLastName());
        commentRepo.save(comment);
        return commentDto;
    }
}
