package com.company.atadu.elibrary.repo;

import com.company.atadu.elibrary.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Long> {
    Rating findRatingByElectronicFileIdAndAppUserUsername(Long id, String username);
    List<Rating> findRatingByElectronicFileId(Long id);
}
