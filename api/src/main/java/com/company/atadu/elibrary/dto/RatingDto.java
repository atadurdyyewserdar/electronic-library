package com.company.atadu.elibrary.dto;

import lombok.Data;

@Data
public class RatingDto {
    private Integer star;
    private Long efileId;
    private String username;

    public RatingDto() {
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
