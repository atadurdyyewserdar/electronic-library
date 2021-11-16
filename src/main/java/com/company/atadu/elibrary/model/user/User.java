package com.company.atadu.elibrary.model.user;

import com.company.atadu.elibrary.model.wishlist.Wishlist;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;

    @OneToMany
    Set<Wishlist> wishlist;
}
