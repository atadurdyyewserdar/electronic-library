package com.company.atadu.elibrary.model.wishlist;

import com.company.atadu.elibrary.model.efile.Efile;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "wishlist")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private Set<Efile> efiles;


}
