package com.company.atadu.elibrary.model.user;

import javax.persistence.*;


@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
}
