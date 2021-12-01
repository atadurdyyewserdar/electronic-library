package com.company.atadu.elibrary.model.efile;

import com.company.atadu.elibrary.model.author.Author;
import com.company.atadu.elibrary.model.category.Category;
import com.company.atadu.elibrary.model.rating.Rating;
import com.company.atadu.elibrary.model.user.AppUser;
import com.company.atadu.elibrary.model.wishlist.Wishlist;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "format_id", referencedColumnName = "id")
    private EfileFormat efileFormat;

    @ManyToMany
    @JoinTable(
            name = "efile_categories",
            joinColumns = @JoinColumn(name = "efile_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "efile_authors",
            joinColumns = @JoinColumn(name = "efile_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @OneToMany(mappedBy = "efile")
    private Set<Rating> ratings;

    private Double averageRating;

    public Efile() {
    }
}
