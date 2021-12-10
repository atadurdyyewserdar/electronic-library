package com.company.atadu.elibrary.repo.rating;

import com.company.atadu.elibrary.model.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Long> {
}
