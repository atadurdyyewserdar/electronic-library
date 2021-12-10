package com.company.atadu.elibrary.service.user.impl;

import com.company.atadu.elibrary.constant.AppUserImplConstant;
import com.company.atadu.elibrary.constant.FileConstant;
import com.company.atadu.elibrary.enumaration.Role;
import com.company.atadu.elibrary.exception.EmailNotFoundException;
import com.company.atadu.elibrary.exception.UserNotFoundException;
import com.company.atadu.elibrary.exception.UsernameExistException;
import com.company.atadu.elibrary.model.user.AppUser;
import com.company.atadu.elibrary.model.user.UserPrincipal;
import com.company.atadu.elibrary.repo.user.AppUserRepo;
import com.company.atadu.elibrary.service.email.EmailService;
import com.company.atadu.elibrary.service.user.AppUserService;
import com.company.atadu.elibrary.service.user.LoginAttemptService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Transactional
@Qualifier("appUserDetailsService")
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private AppUserRepo appUserRepo;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;
    private Logger LOGGER = LoggerFactory.getLogger(getClass()); //UserService.class
    private EmailService emailService;

    @Autowired
    public AppUserServiceImpl(AppUserRepo appUserRepo,
                              BCryptPasswordEncoder passwordEncoder,
                              LoginAttemptService loginAttemptService,
                              EmailService emailService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepo = appUserRepo;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }

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

    @Override
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

    @Override
    public List<AppUser> getUsers() {
        return appUserRepo.findAll();
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return appUserRepo.findUserByUsername(username);
    }

    @Override
    public AppUser findUserByEmail(String email) {
        return appUserRepo.findUserByEmail(email);
    }

    @Override
    public AppUser addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, IOException {
        validateNewUsernameAndEmail(AppUserImplConstant.EMPTY, username, email);
        AppUser appUser = new AppUser();
        String password = generatePassword();
        appUser.setUserId(generateUserId());
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setJoinDate(new Date());
        appUser.setUsername(username);
        appUser.setEmail(email);
        appUser.setPassword(encodePassword(password));
        appUser.setActive(isActive);
        appUser.setNotLocked(isNonLocked);
        appUser.setRole(getRoleEnumName(role).name());
        appUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        appUser.setProfileImageUrl(getTemporaryProfileImageURL(username));
        appUserRepo.save(appUser);
        saveProfileImage(appUser, profileImage);
        return appUser;
    }

    @Override
    public AppUser updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isNonLocked, boolean isActive, MultipartFile newProfileImage) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, IOException {
        AppUser currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setJoinDate(new Date());
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNonLocked);
        currentUser.setRole(getRoleEnumName(newRole).name());
        currentUser.setAuthorities(getRoleEnumName(newRole).getAuthorities());
        currentUser.setWishlists(new ArrayList<>());
        appUserRepo.save(currentUser);
        saveProfileImage(currentUser, newProfileImage);
        return currentUser;
    }

    @Override
    public AppUser updateUserById(Long id, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isNonLocked, boolean isActive, MultipartFile newProfileImage) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, IOException {
        AppUser user = appUserRepo.findById(id).orElseThrow(() -> new UserNotFoundException("Could not find user with id: " + id));
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        user.setJoinDate(new Date());
        user.setUsername(newUsername);
        user.setEmail(newEmail);
        user.setActive(isActive);
        user.setNotLocked(isNonLocked);
        user.setRole(getRoleEnumName(newRole).name());
        user.setAuthorities(getRoleEnumName(newRole).getAuthorities());
        appUserRepo.save(user);
        saveProfileImage(user, newProfileImage);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        appUserRepo.deleteById(id);
    }

    @Override
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

    @Override
    public AppUser updateProfileImage(String username, MultipartFile newImage) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, IOException {
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

    private AppUser validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailNotFoundException {
        AppUser userByUsername = findUserByUsername(newUsername);
        AppUser userByEmail = findUserByEmail(newEmail);
        if (StringUtils.isNotBlank(currentUsername)) {
            AppUser currentUser = findUserByUsername(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(AppUserImplConstant.NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if (userByUsername != null && !currentUser.getId().equals(userByUsername.getId())) {
                throw new UsernameExistException(AppUserImplConstant.USERNAME_ALREADY_EXISTS);
            }
            if (userByEmail != null && !currentUser.getId().equals(userByEmail.getId())) {
                throw new EmailNotFoundException(AppUserImplConstant.EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if (userByUsername != null) {
                throw new UsernameExistException(AppUserImplConstant.USERNAME_ALREADY_EXISTS);
            }
            if (userByEmail != null) {
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
}
