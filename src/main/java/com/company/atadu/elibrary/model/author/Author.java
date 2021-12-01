package com.company.atadu.elibrary.model.author;

import com.company.atadu.elibrary.model.efile.Efile;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @ManyToMany(mappedBy = "authors")
    private Set<Efile> efiles;
}
