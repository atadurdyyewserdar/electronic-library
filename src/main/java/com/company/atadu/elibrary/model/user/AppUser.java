package com.company.atadu.elibrary.model.user;

import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.rating.Rating;

import javax.persistence.*;
import java.util.Set;


@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;

    @OneToMany(mappedBy = "appUser")
    private Set<Efile> efiles;

    @OneToMany(mappedBy = "appUser")
    private Set<Rating> ratings;

    public AppUser() {
    }
}
