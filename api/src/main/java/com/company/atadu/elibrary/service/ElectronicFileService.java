package com.company.atadu.elibrary.service;

import com.company.atadu.elibrary.dto.CommentDto;
import com.company.atadu.elibrary.dto.ElectronicFileDto;
import com.company.atadu.elibrary.dto.ElectronicFileWithComments;
import com.company.atadu.elibrary.dto.RatingDto;
import com.company.atadu.elibrary.model.ElectronicFile;
import com.company.atadu.elibrary.model.ElectronicFileFormat;
import com.company.atadu.elibrary.model.Rating;
import com.company.atadu.elibrary.repo.ElectronicFileRepo;
import com.company.atadu.elibrary.repo.FileResourceRepo;
import com.company.atadu.elibrary.repo.AppUserRepo;
import com.company.atadu.elibrary.repo.RatingRepo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.company.atadu.elibrary.constant.FileConstant.DIRECTORY;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class ElectronicFileService {
    private final ElectronicFileRepo electronicFileRepo;
    private final FileResourceRepo fileResourceRepo;
    private final AppUserRepo appUserRepo;

    private final RatingRepo ratingRepo;
    private final CommentService commentService;

    public ElectronicFileService(ElectronicFileRepo electronicFileRepo, AppUserRepo appUserRepo, FileResourceRepo fileResourceRepo, RatingRepo ratingRepo, CommentService commentService) {
        this.electronicFileRepo = electronicFileRepo;
        this.fileResourceRepo = fileResourceRepo;
        this.appUserRepo = appUserRepo;
        this.ratingRepo = ratingRepo;
        this.commentService = commentService;
    }

    //throw only runtime
    public String saveEfile(MultipartFile multipartFile, String username) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Path fileStorage = get(DIRECTORY, filename).toAbsolutePath().normalize();
        copy(multipartFile.getInputStream(), fileStorage, REPLACE_EXISTING);
        ElectronicFile efile = new ElectronicFile();
        efile.setElectronicFileFormat(ElectronicFileFormat.PDF);
        efile.setAverageRating(0D);
        efile.setName(filename);
        efile.setLocationUrl(fileStorage.toString());
        efile.setAppUser(appUserRepo.findUserByUsername(username));
        efile.setPublishedTime(convertToLocalDateTimeViaInstant(new Date()));
        fileResourceRepo.save(efile);
        return filename;
    }

    public ElectronicFile getBookById(Long id) throws com.company.atadu.elibrary.exception.FileNotFoundException {
        return electronicFileRepo.findById(id).orElseThrow(
                () -> new com.company.atadu.elibrary.exception.FileNotFoundException(
                        "Couldn't find book with id " + id)
        );
    }

    public ElectronicFileWithComments getBookByIdAndComments(Long id) throws com.company.atadu.elibrary.exception.FileNotFoundException {
        ElectronicFile efile = getBookById(id);
        List<CommentDto> comments = commentService.getCommentsOnBook(id);
        ElectronicFileDto eFileDto = new ElectronicFileDto(efile);
        eFileDto.setAverageRating(calculateAvgRating(id));
        return new ElectronicFileWithComments(eFileDto, comments, new RatingDto());
    }

    public Double calculateAvgRating(Long bookId) {
        List<Rating> rates = ratingRepo.findRatingByElectronicFileId(bookId);
        double sum = 0.0;
        for (Rating r : rates) {
            sum += r.getStars();
        }
        return sum / rates.size();
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

    public Resource getFile(Long id) throws IOException {
        ElectronicFileDto eFile = new ElectronicFileDto(electronicFileRepo.getById(id));
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(eFile.getLocationUrl());
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(eFile.getBookName() + " was not found on the server");
        }
        return new UrlResource(filePath.toUri());
    }

    public List<ElectronicFileDto> getAllFiles(int pagination) {
        Pageable pageable = PageRequest.of(pagination, 10).withSort(Sort.by("id"));
        List<ElectronicFile> files = fileResourceRepo.findAll(pageable).getContent();
        List<ElectronicFileDto> list = new ArrayList<>();
        for (ElectronicFile e : files) {
            list.add(new ElectronicFileDto(e));
        }
        return list;
    }

    public List<ElectronicFileDto> getFilteredFiles(int pagination, String authorName, LocalDateTime date, Double rating) {
        Pageable pageable = PageRequest.of(pagination, 10).withSort(Sort.by("id"));
        List<ElectronicFile> files = electronicFileRepo
                .findAllByAuthors_FirstNameAndPublishedTimeBeforeAndAverageRatingLessThan(
                        authorName,
                        date,
                        rating,
                        pageable);
        return files.stream().map(ElectronicFileDto::new).collect(Collectors.toList());
    }

    public List<ElectronicFileDto> searchBookByName(String bookName) {
        List<ElectronicFile> files = electronicFileRepo.getElectronicFilesByNameContaining(bookName);
        return files.stream().map(ElectronicFileDto::new).collect(Collectors.toList());
    }

    public List<ElectronicFileDto> getFilteredFiles() {
        List<ElectronicFile> files = electronicFileRepo
                .findAll();
        return files.stream().map(ElectronicFileDto::new).collect(Collectors.toList());
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
