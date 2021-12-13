package com.company.atadu.elibrary.service;

import com.company.atadu.elibrary.constant.AppUserImplConstant;
import com.company.atadu.elibrary.constant.FileConstant;
import com.company.atadu.elibrary.dto.AppUserDto;
import com.company.atadu.elibrary.enumaration.Role;
import com.company.atadu.elibrary.exception.EmailNotFoundException;
import com.company.atadu.elibrary.exception.UserNotFoundException;
import com.company.atadu.elibrary.exception.UsernameExistException;
import com.company.atadu.elibrary.model.AppUser;
import com.company.atadu.elibrary.model.UserPrincipal;
import com.company.atadu.elibrary.repo.AppUserRepo;
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
public class AppUserService implements UserDetailsService {

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
        AppUser appUser = appUserRepo.findUserByUsername(username);
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

    public AppUser register(String firstname, String lastName, String username, String email) throws UserNotFoundException,
            EmailNotFoundException, UsernameExistException, MessagingException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
        AppUser appUser = new AppUser();
        appUser.setUserId(generateUserId());
        String password = generatePassword();
        appUser.setFirstName(firstname);
        appUser.setLastName(lastName);
        appUser.setUsername(username);
        appUser.setEmail(email);
        appUser.setJoinDate(new Date());
        appUser.setPassword(encodePassword(password));
        appUser.setActive(true);
        appUser.setNotLocked(true);
        appUser.setRole(Role.ROLE_USER.name());
        appUser.setAuthorities(Role.ROLE_USER.getAuthorities());
        appUser.setProfileImageUrl(getTemporaryProfileImageURL(username));
        appUserRepo.save(appUser);
        emailService.sendNewPasswordEmail(firstname, password, email);
        return appUser;
    }

    public List<AppUser> getUsers() {
        return appUserRepo.findAll();
    }

    public AppUser findUserByUsername(String username) {
        return appUserRepo.findUserByUsername(username);
    }

    public AppUser findUserByEmail(String email) {
        return appUserRepo.findUserByEmail(email);
    }

    public AppUser addNewUser(AppUserDto newUser) throws IOException {
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

    public void deleteUser(long id) {
        appUserRepo.deleteById(id);
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

    public AppUser updateProfileImage(String username, MultipartFile newImage) throws IOException {
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

    private AppUser validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) {
        AppUser userByUsername = findUserByUsername(newUsername);
        AppUser userByEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            AppUser currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                try {
                    throw new UserNotFoundException(AppUserImplConstant.NO_USER_FOUND_BY_USERNAME + currentUsername);
                } catch (UserNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (userByUsername != null && !currentUser.getId().equals(userByUsername.getId())) {
                try {
                    throw new UsernameExistException(AppUserImplConstant.USERNAME_ALREADY_EXISTS);
                } catch (UsernameExistException e) {
                    e.printStackTrace();
                }
            }
            if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {
                try {
                    throw new EmailNotFoundException(AppUserImplConstant.EMAIL_ALREADY_EXISTS);
                } catch (EmailNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return currentUser;
        } else {
            if (userByUsername != null) {
                try {
                    throw new UsernameExistException(AppUserImplConstant.USERNAME_ALREADY_EXISTS);
                } catch (UsernameExistException e) {
                    e.printStackTrace();
                }
            }
            if (userByEmail != null) {
                try {
                    throw new EmailNotFoundException(AppUserImplConstant.EMAIL_ALREADY_EXISTS);
                } catch (EmailNotFoundException e) {
                    e.printStackTrace();
                }
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
