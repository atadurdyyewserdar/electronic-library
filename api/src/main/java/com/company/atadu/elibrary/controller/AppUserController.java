package com.company.atadu.elibrary.controller;

import com.company.atadu.elibrary.constant.FileConstant;
import com.company.atadu.elibrary.constant.SecurityConstant;
import com.company.atadu.elibrary.dto.AppUserDto;
import com.company.atadu.elibrary.dto.LoginDto;
import com.company.atadu.elibrary.dto.RegisterDto;
import com.company.atadu.elibrary.exception.EmailNotFoundException;
import com.company.atadu.elibrary.exception.UserNotFoundException;
import com.company.atadu.elibrary.exception.UsernameExistException;
import com.company.atadu.elibrary.model.AppUser;
import com.company.atadu.elibrary.model.HttpResponse;
import com.company.atadu.elibrary.model.UserPrincipal;
import com.company.atadu.elibrary.service.AppUserService;
import com.company.atadu.elibrary.util.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = {"/", "/user"})
@CrossOrigin(origins = "*")
public class AppUserController {
    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";

    @Autowired
    private AppUserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> register(@RequestBody RegisterDto user) throws MessagingException, UserNotFoundException, EmailNotFoundException, UsernameExistException {
        System.out.println(user.toString());
        return ResponseEntity.ok().body(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody AppUser user) {
        authenticate(user.getUsername(), user.getPassword());
        LoginDto loginUser = userService.getUserInLoginDto(user.getUsername());
        AppUser appUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(appUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal, loginUser);
        return ResponseEntity.ok().headers(jwtHeader).body(loginUser);
    }

    @PostMapping("/add")
    public ResponseEntity<AppUser> addNewUser(AppUserDto newUser) throws IOException, UserNotFoundException, EmailNotFoundException, UsernameExistException {
        AppUser user = userService.addNewUser(newUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<AppUser> updateById(@RequestBody AppUserDto appUserDto, @PathVariable("id") Long id)
            throws UserNotFoundException {
        AppUser updatedUser = userService.updateUserById(appUserDto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<AppUser> getUser(@PathVariable("username") String username) {
        AppUser user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) throws EmailNotFoundException, MessagingException {
        userService.resetPassword(email);
        return response(HttpStatus.OK, EMAIL_SENT + email);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<Long> deleteUser(@PathVariable("id") long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<AppUser> updateProfileImage(@RequestParam("username") String username,
                                                      @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, EmailNotFoundException, IOException, UsernameExistException {
        AppUser user = userService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/image/{username}/{filename}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("filename") String fileName) {
        return userService.getProfileImageInBytes(username, fileName);
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String s) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                s.toUpperCase()), httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user, LoginDto loginDto) {
        HttpHeaders headers = new HttpHeaders();
        String token = tokenProvider.generateJwtToken(user);
        loginDto.setToken(token);
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, token);
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
