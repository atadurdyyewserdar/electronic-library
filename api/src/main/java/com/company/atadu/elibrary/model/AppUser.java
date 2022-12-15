package com.company.atadu.elibrary.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Data
public class AppUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
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

    @OneToMany(mappedBy = "appUser")
    private Set<ElectronicFile> electronicFiles;

    @OneToMany(mappedBy = "appUser")
    private Set<Rating> ratings;

    @OneToMany(mappedBy = "appUser")
    private List<Wishlist> wishlists;

    public AppUser() {
    }
}
