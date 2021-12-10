package com.company.atadu.elibrary.service.resource;

import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.efile.EfileFormat;
import com.company.atadu.elibrary.model.user.UserPrincipal;
import com.company.atadu.elibrary.repo.resource.FileResourceRepo;
import com.company.atadu.elibrary.repo.user.AppUserRepo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.company.atadu.elibrary.controller.resource.FileResource.DIRECTORY;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class FileResourceService {
    private FileResourceRepo fileResourceRepo;
    private AppUserRepo appUserRepo;

    @Autowired
    public FileResourceService(FileResourceRepo fileResourceRepo,
                               AppUserRepo appUserRepo
    ) {
        this.fileResourceRepo = fileResourceRepo;
        this.appUserRepo = appUserRepo;
    }

    //move multipart to controller
    //throw only runtime
    public String saveEfile(MultipartFile multipartFile, String username) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
        copy(multipartFile.getInputStream(), fileStorage, REPLACE_EXISTING);
        Efile efile = new Efile();
        efile.setEfileFormat(EfileFormat.PDF);
        efile.setAverageRating(0D);
        efile.setName(filename);
        efile.setLocationUrl(fileStorage.toString());
        efile.setAppUser(appUserRepo.findUserByUsername(username));
        efile.setPublishedTime(convertToLocalDateTimeViaInstant(new Date()));
        fileResourceRepo.save(efile);
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

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
