package com.company.atadu.elibrary.model.rating;

import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.user.AppUser;

import javax.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "efile_id")
    private Efile efile;

    private Integer stars;
}
