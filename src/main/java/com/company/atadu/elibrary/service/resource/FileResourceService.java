package com.company.atadu.elibrary.service.resource;

import com.company.atadu.elibrary.dto.efile.EfileDto;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.company.atadu.elibrary.controller.resource.FileResource.DIRECTORY;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class FileResourceService {
    //private FileResourceRepo fileResourceRepo;

//    public FileResourceService(FileResourceRepo fileResourceRepo) {
//        this.fileResourceRepo = fileResourceRepo;
//    }

    public String saveEfile(MultipartFile multipartFile, EfileDto efileDto) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
        copy(multipartFile.getInputStream(), fileStorage, REPLACE_EXISTING);
        return filename;
    }

    public List<String> saveMultipleEfiles(List<MultipartFile> multipartFiles) throws IOException {
        List<String> filenames = new ArrayList<>();
        String filename;
        for (MultipartFile file : multipartFiles) {
            filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            filenames.add(filename);
        }
        return filenames;
    }

    public Resource getFile(String filename) throws IOException {
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(filename);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        return new UrlResource(filePath.toUri());
    }
}
