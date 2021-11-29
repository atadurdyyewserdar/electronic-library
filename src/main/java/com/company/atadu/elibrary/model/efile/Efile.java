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

}
