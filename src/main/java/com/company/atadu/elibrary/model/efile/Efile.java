package com.company.atadu.elibrary.model.efile;

import com.company.atadu.elibrary.model.category.Category;
import com.company.atadu.elibrary.model.wishlist.Wishlist;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "efiles")
public class Efile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private LocalDateTime created_time;

    @OneToOne
    @JoinColumn(name = "file_format_id", referencedColumnName = "id")
    private EfileFormat fileFormat;

    @OneToOne
    @JoinColumn(name = "file_type_id", referencedColumnName = "id")
    private EfileType efileType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;
}
