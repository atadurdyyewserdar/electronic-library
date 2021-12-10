package com.company.atadu.elibrary.service.rating;

import com.company.atadu.elibrary.dto.rating.RatingDto;
import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.rating.Rating;
import com.company.atadu.elibrary.model.user.UserPrincipal;
import com.company.atadu.elibrary.repo.efile.EfileRepo;
import com.company.atadu.elibrary.repo.rating.RatingRepo;
import com.company.atadu.elibrary.repo.user.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private RatingRepo ratingRepo;
    private EfileRepo efileRepo;
    private AppUserRepo appUserRepo;

    @Autowired
    public RatingService(RatingRepo ratingRepo,
                         EfileRepo efileRepo,
                         AppUserRepo appUserRepo) {
        this.ratingRepo = ratingRepo;
        this.efileRepo = efileRepo;
        this.appUserRepo = appUserRepo;
    }

    //refactor
    public RatingDto rateEfile(RatingDto ratingDto) {
        Rating rating = new Rating();
        Efile efile = efileRepo.getById(ratingDto.getEfileId());
        double temp = 0;
        double result = 0;
        for (Rating r : efile.getRatings()) {
            temp += r.getStars();
        }
        result = (temp + ratingDto.getStar()) / (efile.getRatings().size() + 1);
        efile.setAverageRating(result);
        efileRepo.save(efile);
        rating.setEfile(efile);
        rating.setAppUser(appUserRepo.findUserByUsername(ratingDto.getUsername()));
        rating.setStars(ratingDto.getStar());
        ratingRepo.save(rating);
        return ratingDto;
    }
}
