package com.company.atadu.elibrary.model.author;

import com.company.atadu.elibrary.model.efile.Efile;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private Set<Efile> efiles;
}
