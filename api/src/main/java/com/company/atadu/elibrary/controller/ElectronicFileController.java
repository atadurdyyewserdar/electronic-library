package com.company.atadu.elibrary.controller;

import com.company.atadu.elibrary.dto.CommentDto;
import com.company.atadu.elibrary.dto.ElectronicFileDto;
import com.company.atadu.elibrary.dto.ElectronicFileWithComments;
import com.company.atadu.elibrary.dto.StringHolderDto;
import com.company.atadu.elibrary.exception.FileNotFoundException;
import com.company.atadu.elibrary.service.CommentService;
import com.company.atadu.elibrary.service.ElectronicFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;


import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/resource")
public class ElectronicFileController {

    private final ElectronicFileService electronicFileService;
    private final CommentService commentService;


    @Autowired
    public ElectronicFileController(ElectronicFileService electronicFileService, CommentService commentService) {
        this.electronicFileService = electronicFileService;
        this.commentService = commentService;
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

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("id") Long id) throws IOException {
        Resource resource = electronicFileService.getFile(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        //        httpHeaders.add("File-Name", filename);
        //        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;");
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .headers(httpHeaders).body(resource);
    }

    /*
        @GetMapping("/books/all")
        public ResponseEntity<List<ElectronicFileDto>> getAllBooks(@RequestParam("page") int pagination,
                                                                   @RequestParam("author") String authorName,
                                                                   @RequestParam("date") LocalDateTime date,
                                                                   @RequestParam("rate") double rate) {
            return ResponseEntity.ok().body(electronicFileService.getFilteredFiles(pagination, authorName, date, rate));
        }
    */
    @GetMapping(value = "/books/all", params = {"bookName"})
    public ResponseEntity<List<ElectronicFileDto>> getAllBooks(@RequestParam("bookName") String bookName) {
        System.out.println("correct");
        return ResponseEntity.ok().body(electronicFileService.searchBookByName(bookName));
    }

    @GetMapping("/books/all")
    public ResponseEntity<List<ElectronicFileDto>> getAllBooks() {
        return ResponseEntity.ok().body(electronicFileService.getFilteredFiles());
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<ElectronicFileWithComments> getBookById(@PathVariable Long id) throws FileNotFoundException {
        return ResponseEntity.ok().body(electronicFileService.getBookByIdAndComments(id));
    }

    @GetMapping("/books/{id}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsOnBook(@PathVariable Long id) {
        return ResponseEntity.ok().body(commentService.getCommentsOnBook(id));
    }

    @PostMapping("books/{id}/comments")
    public ResponseEntity<CommentDto> commentTheBook(@PathVariable Long id, @RequestBody CommentDto commentDto) throws java.io.FileNotFoundException {
        return ResponseEntity.ok().body(commentService.saveComment(commentDto, id));
    }
}