package com.company.atadu.elibrary.dto;

import com.company.atadu.elibrary.model.AppUser;
import lombok.Data;

@Data
public class AppUserDto {
    private Long id;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String profileImageUrl;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

    public AppUserDto() {
    }

    public AppUserDto(Long id, String email, String userId, String firstName, String lastName,
                      String username, String profileImageUrl,  String role, String[] authorities,
                      boolean isActive, boolean isNotLocked) {
        this.id = id;
        this.email = email;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.isNotLocked = isNotLocked;
    }

    public AppUserDto(AppUser appUser) {
        this.id = appUser.getId();
        this.email = appUser.getEmail();
        this.userId = appUser.getUserId();
        this.firstName = appUser.getFirstName();
        this.lastName = appUser.getLastName();
        this.username = appUser.getUsername();
        this.profileImageUrl = appUser.getProfileImageUrl();
        this.role = appUser.getRole();
        this.authorities = appUser.getAuthorities();
        this.isActive = appUser.isActive();
        this.isNotLocked = appUser.isNotLocked();
    }
}
