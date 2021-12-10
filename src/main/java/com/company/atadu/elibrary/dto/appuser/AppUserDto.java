package com.company.atadu.elibrary.dto.appuser;

import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.rating.Rating;
import com.company.atadu.elibrary.model.user.AppUser;
import com.company.atadu.elibrary.model.wishlist.Wishlist;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class AppUserDto {
    private Long id;
    private String email;
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;
    private Set<Long> efiles;
    private List<Long> ratings;
    private List<String> wishlists;

    public AppUserDto() {
    }

    public AppUserDto(Long id, String email, String userId, String firstName, String lastName, String username, String password, String profileImageUrl, Date lastLoginDate, Date lastLoginDateDisplay, Date joinDate, String role, String[] authorities, boolean isActive, boolean isNotLocked) {
        this.id = id;
        this.email = email;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.lastLoginDate = lastLoginDate;
        this.lastLoginDateDisplay = lastLoginDateDisplay;
        this.joinDate = joinDate;
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
        this.password = appUser.getPassword();
        this.profileImageUrl = appUser.getProfileImageUrl();
        this.lastLoginDate = appUser.getLastLoginDate();
        this.lastLoginDateDisplay = appUser.getLastLoginDateDisplay();
        this.joinDate = appUser.getJoinDate();
        this.role = appUser.getRole();
        this.authorities = appUser.getAuthorities();
        this.isActive = appUser.isActive();
        this.isNotLocked = appUser.isNotLocked();
    }
}
