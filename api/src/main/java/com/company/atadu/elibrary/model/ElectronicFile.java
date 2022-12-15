package com.company.atadu.elibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "electronic_files")
public class ElectronicFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    private LocalDateTime publishedTime;
    private String locationUrl;
    private String iconUrl;

    @Enumerated(EnumType.STRING)
    private ElectronicFileFormat electronicFileFormat;

    @ManyToMany
    @JoinTable(
            name = "electronic_file_authors",
            joinColumns = @JoinColumn(name = "electronic_file_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonIgnore
    private Set<Author> authors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    @JsonIgnore
    private AppUser appUser;

    @OneToMany(mappedBy = "electronicFile")
    @JsonIgnore
    private Set<Rating> ratings;

    @ManyToMany(mappedBy = "electronicFiles")
    @JsonIgnore
    private Set<Wishlist> wishlists;

    private Double averageRating;

    private String description;

    public ElectronicFile() {
    }
}
