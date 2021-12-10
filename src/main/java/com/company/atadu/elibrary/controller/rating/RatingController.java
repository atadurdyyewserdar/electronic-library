package com.company.atadu.elibrary.controller.rating;

import com.company.atadu.elibrary.dto.rating.RatingDto;
import com.company.atadu.elibrary.service.rating.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/", "/user"})
@CrossOrigin(origins = "*")
public class RatingController {

    private RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/rate-efile")
    public ResponseEntity<RatingDto> rateEfile(@RequestBody RatingDto ratingDto) {
        RatingDto ratingDtoRes = ratingService.rateEfile(ratingDto);
        return new ResponseEntity<>(ratingDtoRes, HttpStatus.OK);
    }
}
