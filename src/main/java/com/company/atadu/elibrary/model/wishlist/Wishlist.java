package com.company.atadu.elibrary.model.wishlist;

import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.hashtag.Tag;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "wishlist_tag",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
