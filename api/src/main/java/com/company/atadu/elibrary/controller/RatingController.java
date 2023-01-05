package com.company.atadu.elibrary.controller;

import com.company.atadu.elibrary.dto.RatingDto;
import com.company.atadu.elibrary.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/resource"})
public class RatingController {

    private RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/books/{id}/rate")
    public ResponseEntity<RatingDto> rateEfile(@PathVariable Long id, @RequestBody RatingDto ratingDto) {
        RatingDto ratingDtoRes = ratingService.rateEfile(ratingDto);
        return new ResponseEntity<>(ratingDtoRes, HttpStatus.OK);
    }

    @GetMapping("/books/{id}/rate/{username}")
    public ResponseEntity<RatingDto> getUserRate(@PathVariable Long id, @PathVariable String username) {
        return new ResponseEntity<>(ratingService.getUserRate(id, username), HttpStatus.OK);
    }
}
