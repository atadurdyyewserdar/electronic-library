package com.company.atadu.elibrary.model.efile;

import javax.persistence.*;

@Entity
@Table(name = "file_format")
public class EfileFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private String format_name;
}
