package com.company.atadu.elibrary.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "electronic_file_id")
    private ElectronicFile electronicFile;

    private String userName;
    private String comment;
    private String firstName;
    private String lastName;
}
