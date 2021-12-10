package com.company.atadu.elibrary.service.user;

import com.company.atadu.elibrary.exception.EmailNotFoundException;
import com.company.atadu.elibrary.exception.UserNotFoundException;
import com.company.atadu.elibrary.exception.UsernameExistException;
import com.company.atadu.elibrary.model.user.AppUser;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

//remove
public interface AppUserService {
    AppUser register(String firstname, String lastName, String username, String email) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, MessagingException, MessagingException;

    List<AppUser> getUsers();

    AppUser findUserByUsername(String username);

    AppUser findUserByEmail(String email);

    AppUser addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, IOException;

    AppUser updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isNonLocked, boolean isActive, MultipartFile newProfileImage) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, IOException;

    AppUser updateUserById(Long id, String newFirstName, String newLastName, String newUsername, String newEmail, String newRole, boolean isNonLocked, boolean isActive, MultipartFile newProfileImage) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, IOException;

    void deleteUser(long id);

    void resetPassword(String email) throws MessagingException, EmailNotFoundException;

    AppUser updateProfileImage(String username, MultipartFile newImage) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, IOException;

}
