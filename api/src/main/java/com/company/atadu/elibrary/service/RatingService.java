package com.company.atadu.elibrary.service;

import com.company.atadu.elibrary.dto.RatingDto;
import com.company.atadu.elibrary.model.ElectronicFile;
import com.company.atadu.elibrary.model.Rating;
import com.company.atadu.elibrary.repo.ElectronicFileRepo;
import com.company.atadu.elibrary.repo.RatingRepo;
import com.company.atadu.elibrary.repo.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private RatingRepo ratingRepo;
    private ElectronicFileRepo electronicFileRepo;
    private AppUserRepo appUserRepo;

    @Autowired
    public RatingService(RatingRepo ratingRepo,
                         ElectronicFileRepo electronicFileRepo,
                         AppUserRepo appUserRepo) {
        this.ratingRepo = ratingRepo;
        this.electronicFileRepo = electronicFileRepo;
        this.appUserRepo = appUserRepo;
    }

    //refactor
    public RatingDto rateEfile(RatingDto ratingDto) {
        Rating rating = new Rating();
        ElectronicFile efile = electronicFileRepo.getById(ratingDto.getEfileId());
        List<Rating> ratings = ratingRepo.findRatingByElectronicFileId(ratingDto.getEfileId());
        double temp = 0;
        double result = 0;
        for (Rating r : ratings) {
            temp += r.getStars();
        }
        result = (temp + ratingDto.getStar()) / (ratings.size() + 1);
        efile.setAverageRating(result);
        electronicFileRepo.save(efile);
        rating.setElectronicFile(efile);
        rating.setAppUser(appUserRepo.findUserByUsername(ratingDto.getUsername()));
        rating.setStars(ratingDto.getStar());
        System.out.println("username is:" + rating.getAppUser().getUsername());
        System.out.println("rating is: " + rating.getStars());
        System.out.println("filename is: " + rating.getElectronicFile().getName());
        ratingRepo.save(rating);
        return ratingDto;
    }

    public RatingDto getUserRate(Long bookId, String username) {
        Rating rate = new Rating();
        if (!username.equals("")) {
            rate = ratingRepo.findRatingByElectronicFileIdAndAppUserUsername(bookId, username);
        }
        return new RatingDto(rate);
    }
}
