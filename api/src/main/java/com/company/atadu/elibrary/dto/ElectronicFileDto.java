package com.company.atadu.elibrary.dto;

import com.company.atadu.elibrary.model.ElectronicFile;
import com.company.atadu.elibrary.model.ElectronicFileFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
public class ElectronicFileDto {
    private Long id;
    private String bookName;
    private LocalDateTime publishedTime;
    private String iconUrl;
    private ElectronicFileFormat electronicFileFormat;
    private String description;
    private Double averageRating;
    private List<AuthorDto> authors;
    private String locationUrl;

    public ElectronicFileDto(ElectronicFile electronicFile) {
        this.id = electronicFile.getId();
        this.bookName = electronicFile.getName();
        this.publishedTime = electronicFile.getPublishedTime();
        this.iconUrl = electronicFile.getIconUrl();
        this.electronicFileFormat = electronicFile.getElectronicFileFormat();
        this.description = electronicFile.getDescription();
        this.averageRating = electronicFile.getAverageRating();
        this.locationUrl = electronicFile.getLocationUrl();
    }
}
