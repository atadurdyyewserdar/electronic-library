package com.company.atadu.elibrary.model.efile;

import javax.persistence.*;

@Entity
@Table(name = "file_type")
public class EfileType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private String type_name;

    @OneToOne(mappedBy = "file_type")
    private Efile efile;
}
