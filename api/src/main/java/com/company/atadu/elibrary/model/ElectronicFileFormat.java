package com.company.atadu.elibrary.model;

public enum ElectronicFileFormat {
    PDF("pdf"),
    MP3("mp3"),
    DOC("doc"),
    DOCX("docx");

    private String name;

    ElectronicFileFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
