package com.company.atadu.elibrary.model.rating;

import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    @JsonIgnore
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "efile_id")
    private Efile efile;

    private Integer stars;

    public Rating() {
    }

    public Rating(Long id, AppUser appUser, Efile efile, Integer stars) {
        this.id = id;
        this.appUser = appUser;
        this.efile = efile;
        this.stars = stars;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Efile getEfile() {
        return efile;
    }

    public void setEfile(Efile efile) {
        this.efile = efile;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }
}
