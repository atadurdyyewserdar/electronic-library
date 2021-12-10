package com.company.atadu.elibrary.model.efile;

import javax.persistence.*;

public enum EfileFormat {
    PDF("pdf"),
    MP3("mp3"),
    DOC("doc"),
    DOCX("docx");

    private String name;

    EfileFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
