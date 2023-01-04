package com.company.atadu.elibrary.service;

import com.company.atadu.elibrary.constant.AppUserImplConstant;
import com.company.atadu.elibrary.constant.FileConstant;
import com.company.atadu.elibrary.dto.AppUserDto;
import com.company.atadu.elibrary.dto.LoginDto;
import com.company.atadu.elibrary.dto.RegisterDto;
import com.company.atadu.elibrary.enumaration.Role;
import com.company.atadu.elibrary.exception.*;
import com.company.atadu.elibrary.model.AppUser;
import com.company.atadu.elibrary.model.UserPrincipal;
import com.company.atadu.elibrary.repo.AppUserRepo;
import com.company.atadu.elibrary.util.DataChecker;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
@Qualifier("appUserDetailsService")
public class AppUserService implements UserService, UserDetailsService {

    @Autowired
    private AppUserRepo appUserRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private LoginAttemptService loginAttemptService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = null;
        if (username.contains("@")) {
            appUser = appUserRepo.findUserByEmail(username);
        } else {
            appUser = appUserRepo.findUserByUsername(username);
        }
        if (appUser == null) {
            LOGGER.error(AppUserImplConstant.NO_USER_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(AppUserImplConstant.NO_USER_FOUND_BY_USERNAME + username);
        } else {
            validateLoginAttempt(appUser);
            appUser.setLastLoginDateDisplay(appUser.getLastLoginDate());
            appUser.setLastLoginDate(new Date());
            appUserRepo.save(appUser);
            UserPrincipal userPrincipal = new UserPrincipal(appUser);
            LOGGER.info(AppUserImplConstant.FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    @Override
    public RegisterDto register(RegisterDto registerDto) throws MessagingException, UserNotFoundException, EmailNotFoundException, UsernameExistException, EmailExistException, InvalidCredentialsException {
        if (!DataChecker.isSecurePassword(registerDto.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        if (!DataChecker.isValidEmail(registerDto.getEmail())) {
            throw new InvalidCredentialsException("Invalid email");
        }
        validateNewUsernameAndEmail(StringUtils.EMPTY, registerDto.getUsername(), registerDto.getEmail());
        AppUser appUser = new AppUser();
        appUser.setUserId(generateUserId());
        // String password = generatePassword();
        // appUser.setPassword(registerDto.getPassword());
        appUser.setPassword(encodePassword(registerDto.getPassword()));
        appUser.setFirstName(registerDto.getFirstName());
        appUser.setLastName(registerDto.getLastName());
        appUser.setUsername(registerDto.getUsername());
        appUser.setEmail(registerDto.getEmail());
        appUser.setJoinDate(new Date());
        appUser.setActive(true);
        appUser.setNotLocked(true);
        appUser.setRole(Role.ROLE_USER.name());
        appUser.setAuthorities(Role.ROLE_USER.getAuthorities());
        appUser.setProfileImageUrl(getTemporaryProfileImageURL(registerDto.getUsername()));
        appUserRepo.save(appUser);
        // emailService.sendNewPasswordEmail(registerDto.getFirstName(), password, registerDto.getEmail());
        return registerDto;
    }

    @Override
    public List<AppUser> getUsers() {
        return appUserRepo.findAll();
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return appUserRepo.findUserByUsername(username);
    }

    public LoginDto getUserInLoginDto(String username) {
        //        AppUser appUser2 = appUserRepo.findUserByEmail("atadurdyyewserdar@gmail.com");
        //        appUser2.setPassword(encodePassword("admin"));
        //        appUserRepo.save(appUser2);
        LoginDto loginDto = new LoginDto();
        AppUser appUser = appUserRepo.findUserByUsername(username);
        loginDto.setId(appUser.getId());
        loginDto.setEmail(appUser.getEmail());
        loginDto.setFirstName(appUser.getFirstName());
        loginDto.setLastName(appUser.getLastName());
        loginDto.setNotLocked(appUser.isNotLocked());
        loginDto.setUsername(appUser.getUsername());
        System.out.println("username is " + appUser.getUsername());
        return loginDto;
    }

    @Override
    public AppUser findUserByEmail(String email) {
        return appUserRepo.findUserByEmail(email);
    }

    public AppUser addNewUser(AppUserDto newUser) throws IOException, UserNotFoundException, EmailNotFoundException, UsernameExistException, EmailExistException {
        validateNewUsernameAndEmail(AppUserImplConstant.EMPTY, newUser.getUsername(), newUser.getEmail());
        AppUser appUser = new AppUser();
        String password = generatePassword();
        appUser.setUserId(generateUserId());
        appUser.setFirstName(newUser.getFirstName());
        appUser.setLastName(newUser.getLastName());
        appUser.setJoinDate(new Date());
        appUser.setUsername(newUser.getUsername());
        appUser.setEmail(newUser.getEmail());
        appUser.setPassword(encodePassword(password));
        appUser.setActive(newUser.isActive());
        appUser.setNotLocked(newUser.isNotLocked());
        appUser.setRole(getRoleEnumName(newUser.getRole()).name());
        appUser.setAuthorities(getRoleEnumName(newUser.getRole()).getAuthorities());
        appUser.setProfileImageUrl(getTemporaryProfileImageURL(newUser.getUsername()));
        appUserRepo.save(appUser);
        return appUser;
    }

    public AppUser updateUserById(AppUserDto appUser, Long id) throws UserNotFoundException {
        AppUser user = appUserRepo.findById(id).orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + id));
        user.setFirstName(appUser.getFirstName());
        user.setLastName(appUser.getLastName());
        user.setUsername(appUser.getUsername());
        user.setEmail(appUser.getEmail());
        user.setActive(appUser.isActive());
        user.setNotLocked(appUser.isNotLocked());
        user.setRole(getRoleEnumName(appUser.getRole()).name());
        user.setAuthorities(getRoleEnumName(appUser.getRole()).getAuthorities());
        appUserRepo.save(user);
        return user;
    }

    public long deleteUser(long id) {
        appUserRepo.deleteById(id);
        return id;
    }

    public void resetPassword(String email) throws MessagingException, EmailNotFoundException {
        AppUser appUser = appUserRepo.findUserByEmail(email);
        if (appUser == null) {
            throw new EmailNotFoundException(AppUserImplConstant.NO_USER_FOUND_BY_EMAIL + email);
        }
        String password = generatePassword();
        appUser.setPassword(encodePassword(password));
        appUserRepo.save(appUser);
        emailService.sendNewPasswordEmail(appUser.getFirstName(), password, appUser.getEmail());
    }

    public AppUser updateProfileImage(String username, MultipartFile newImage) throws IOException, UserNotFoundException, EmailNotFoundException, UsernameExistException, EmailExistException {
        AppUser appUser = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(appUser, newImage);
        return appUser;
    }

    private void saveProfileImage(AppUser appUser, MultipartFile profileImage) throws IOException {
        if (profileImage != null) {
            Path userFolder = Paths.get(FileConstant.USER_FOLDER + appUser.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.info(FileConstant.DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + appUser.getUsername() + FileConstant.DOT + FileConstant.JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(appUser.getUsername() + FileConstant.DOT + FileConstant.JPG_EXTENSION), REPLACE_EXISTING);
            appUser.setProfileImageUrl(setProfileImageURL(appUser.getUsername()));
            appUserRepo.save(appUser);
            LOGGER.info(FileConstant.FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageURL(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileConstant.USER_IMAGE_PATH + username + FileConstant.FORWARD_SLASH
                        + username + FileConstant.DOT + FileConstant.JPG_EXTENSION).toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String getTemporaryProfileImageURL(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileConstant.DEFAULT_USER_IMAGE_PATH + username).toUriString();
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private AppUser validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailNotFoundException, EmailExistException {
        AppUser userByUsername = findUserByUsername(newUsername);
        AppUser userByEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            AppUser currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(AppUserImplConstant.NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByUsername != null && !currentUser.getId().equals(userByUsername.getId())) {
                System.out.println("exc here 1");
                throw new UsernameExistException(AppUserImplConstant.USERNAME_ALREADY_EXISTS);
            }
            if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {
                System.out.println("here 2");
                throw new EmailNotFoundException(AppUserImplConstant.EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByUsername != null) {
                System.out.println("here 3");
                throw new UsernameExistException(AppUserImplConstant.USERNAME_ALREADY_EXISTS);
            }
            if (userByEmail != null) {
                System.out.println("here 4");
                throw new EmailNotFoundException(AppUserImplConstant.EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private void validateLoginAttempt(AppUser appUser) {
        if (appUser.isNotLocked()) {
            appUser.setNotLocked(!loginAttemptService.hasExceededMaxAttempts(appUser.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(appUser.getUsername());
        }
    }

    public byte[] getProfileImageInBytes(String username, String fileName) {
        byte[] imageBytes = null;
        try {
            imageBytes = Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER + username + FileConstant.FORWARD_SLASH + fileName));
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return imageBytes;
    }
}
