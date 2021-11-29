package com.company.atadu.elibrary.controller.resource;

import com.company.atadu.elibrary.dto.efile.EfileDto;
import com.company.atadu.elibrary.service.resource.FileResourceService;
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
public class FileResource {

    //Location definition
    public static final String DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";
    private FileResourceService fileResourceService;

    public FileResource(FileResourceService fileResourceService) {
        this.fileResourceService = fileResourceService;
    }

    @PostMapping("/upload-single-file")
    public ResponseEntity<String> uploadFile(@RequestParam("files") MultipartFile multipartFile, @RequestBody EfileDto efileDto) throws IOException {
        String filename = fileResourceService.saveEfile(multipartFile, efileDto);
        return ResponseEntity.ok().body(filename);
    }

    @PostMapping("/upload-multiple-files")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = fileResourceService.saveMultipleEfiles(multipartFiles);
        return ResponseEntity.ok().body(filenames);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Resource resource = fileResourceService.getFile(filename);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .headers(httpHeaders).body(resource);
    }
}