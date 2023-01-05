package com.company.atadu.elibrary.repo;

import com.company.atadu.elibrary.model.ElectronicFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ElectronicFileRepo extends JpaRepository<ElectronicFile, Long> {
    List<ElectronicFile> findAllByAuthors_FirstNameAndPublishedTimeBeforeAndAverageRatingLessThan(
            String firstName,
            LocalDateTime localDateTime,
            Double rating,
            Pageable pageable
    );

    List<ElectronicFile> findAllByName(String bookName);
    List<ElectronicFile> getElectronicFilesByNameContaining(String bookName);

}
