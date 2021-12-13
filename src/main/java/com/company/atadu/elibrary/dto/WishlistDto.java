package com.company.atadu.elibrary.dto;

import lombok.Data;

import java.util.Set;

@Data
public class WishlistDto {
    private Long id;
    private String userName;
    private String wishlistName;
    private boolean isPrivate;
    private Set<String> tags;

    public WishlistDto() {
    }
}
