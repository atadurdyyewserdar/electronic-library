package com.company.atadu.elibrary.model.category;

import com.company.atadu.elibrary.model.efile.Efile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime createdTime;

    @ManyToMany(mappedBy = "categories")
    private Set<Efile> efiles;
}
