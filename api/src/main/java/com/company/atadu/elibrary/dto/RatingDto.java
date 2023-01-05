package com.company.atadu.elibrary.dto;

import com.company.atadu.elibrary.model.Rating;
import lombok.Data;

@Data
public class RatingDto {
    private Integer star;
    private Long efileId;
    private String username;

    public RatingDto() {
        this.username = "";
        this.efileId = -1L;
        this.star = -1;
    }

    public RatingDto(Rating rate) {
        if (rate != null) {
            this.efileId = rate.getAppUser().getId();
            this.star = rate.getStars();
            this.username = rate.getAppUser().getUsername();
            return;
        }
        this.username = "";
        this.efileId = -1L;
        this.star = -1;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Long getEfileId() {
        return efileId;
    }

    public void setEfileId(Long efileId) {
        this.efileId = efileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
