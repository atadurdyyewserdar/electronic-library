package com.company.atadu.elibrary.model.efile;

import com.company.atadu.elibrary.model.author.Author;
import com.company.atadu.elibrary.model.rating.Rating;
import com.company.atadu.elibrary.model.user.AppUser;
import com.company.atadu.elibrary.model.wishlist.Wishlist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "efiles")
public class Efile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    private LocalDateTime publishedTime;
    private String locationUrl;
    private String iconUrl;

    @Enumerated(EnumType.STRING)
    private EfileFormat efileFormat;

    @ManyToMany
    @JoinTable(
            name = "efile_authors",
            joinColumns = @JoinColumn(name = "efile_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonIgnore
    private Set<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    @JsonIgnore
    private AppUser appUser;

    @OneToMany(mappedBy = "efile")
    @JsonIgnore
    private Set<Rating> ratings;

    @ManyToMany(mappedBy = "efiles")
    @JsonIgnore
    private Set<Wishlist> wishlists;

    private Double averageRating;

    public Efile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(LocalDateTime publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public EfileFormat getEfileFormat() {
        return efileFormat;
    }

    public void setEfileFormat(EfileFormat efileFormat) {
        this.efileFormat = efileFormat;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Set<Wishlist> getWishlists() {
        return wishlists;
    }

    public void setWishlists(Set<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
