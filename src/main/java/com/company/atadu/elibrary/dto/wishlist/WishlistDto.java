package com.company.atadu.elibrary.dto.wishlist;

import java.util.Set;

public class WishlistDto {
    private String wishlistName;
    private boolean isPrivate;
    private Set<String> tags;

    public WishlistDto() {

    }

    public WishlistDto(String wishlistName, boolean isPrivate, Set<String> tags) {
        this.wishlistName = wishlistName;
        this.isPrivate = isPrivate;
        this.tags = tags;
    }

    public String getWishlistName() {
        return wishlistName;
    }

    public void setWishlistName(String wishlistName) {
        this.wishlistName = wishlistName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
