package com.company.atadu.elibrary.model.category;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime created_time;
}
