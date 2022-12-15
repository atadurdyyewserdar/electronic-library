package com.company.atadu.elibrary.controller;

import com.company.atadu.elibrary.dto.ElectronicFileDto;
import com.company.atadu.elibrary.service.ElectronicFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.List;


import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/resource")
public class ElectronicFileController {

    @Autowired
    private ElectronicFileService electronicFileService;

    public ElectronicFileController(ElectronicFileService electronicFileService) {
        this.electronicFileService = electronicFileService;
    }

    //try catch in service and throw as runtime ex
    @PostMapping("/upload-single-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                             @RequestParam("username") String username) throws IOException {
        String filename = electronicFileService.saveEfile(multipartFile, username);
        return ResponseEntity.ok().body(filename);
    }

    @PostMapping("/upload-multiple-files")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = electronicFileService.saveMultipleEfiles(multipartFiles);
        return ResponseEntity.ok().body(filenames);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Resource resource = electronicFileService.getFile(filename);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .headers(httpHeaders).body(resource);
    }

    @GetMapping("/books/all")
    public ResponseEntity<List<ElectronicFileDto>> getAllBooks(@RequestParam("page") int pagination,
                                                               @RequestParam("author") String authorName,
                                                               @RequestParam("date") LocalDateTime date,
                                                               @RequestParam("rate") double rate) {
        return ResponseEntity.ok().body(electronicFileService.getFilteredFiles(pagination, authorName, date, rate));
    }
}