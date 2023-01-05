package com.company.atadu.elibrary.service;

import com.company.atadu.elibrary.dto.RegisterDto;
import com.company.atadu.elibrary.exception.*;
import com.company.atadu.elibrary.model.AppUser;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
    RegisterDto register(RegisterDto registerDto) throws MessagingException, UserNotFoundException, EmailNotFoundException, UsernameExistException, EmailExistException, InvalidCredentialsException;
    List<AppUser> getUsers();
    AppUser findUserByUsername(String username);
    AppUser findUserByEmail(String email);

}
