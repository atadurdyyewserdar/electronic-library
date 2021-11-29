package com.company.atadu.elibrary.model.hashtag;

import com.company.atadu.elibrary.model.wishlist.Wishlist;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tag_name")
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    Set<Wishlist> wishlists;
}
