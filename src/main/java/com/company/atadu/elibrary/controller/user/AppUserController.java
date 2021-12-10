package com.company.atadu.elibrary.controller.user;

import com.company.atadu.elibrary.constant.FileConstant;
import com.company.atadu.elibrary.constant.SecurityConstant;
import com.company.atadu.elibrary.dto.wishlist.WishlistDto;
import com.company.atadu.elibrary.exception.EmailNotFoundException;
import com.company.atadu.elibrary.exception.UserNotFoundException;
import com.company.atadu.elibrary.exception.UsernameExistException;
import com.company.atadu.elibrary.model.user.AppUser;
import com.company.atadu.elibrary.model.user.HttpResponse;
import com.company.atadu.elibrary.model.user.UserPrincipal;
import com.company.atadu.elibrary.model.wishlist.Wishlist;
import com.company.atadu.elibrary.service.user.AppUserService;
import com.company.atadu.elibrary.service.wishlist.WishlistService;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = {"/", "/user"})
@CrossOrigin(origins = "*")
public class AppUserController {
    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private final AppUserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider tokenProvider;
    private WishlistService wishlistService;

    @Autowired
    public AppUserController(AppUserService userService,
                             AuthenticationManager authenticationManager,
                             JWTTokenProvider tokenProvider,
                             WishlistService wishlistService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.wishlistService = wishlistService;
    }

    @PostMapping("/create-wishlist")
    public ResponseEntity<String> createWishlist(@RequestBody WishlistDto wishlistDto) {
        String result = wishlistService.createWishlist(wishlistDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/add-efile-to-wishlist")
    public ResponseEntity<HttpResponse> addToWishlist(@RequestParam("efileId") Long fileId,
                                                      @RequestParam("wishlistId") Long wishlistId) {
        wishlistService.addNewItemToWishlist(fileId, wishlistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-from-wishlist/{wishlistId}/{efileId}")
    public ResponseEntity<HttpResponse> removeFromWishlist(@PathVariable Long wishlistId,
                                                           @PathVariable Long efileId) {
        wishlistService.removeFromWishlist(wishlistId, efileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-wishlist/{id}")
    public ResponseEntity<HttpResponse> deleteWishlist(@PathVariable Long id) {
        wishlistService.deleteWishlist(id);
        return response(HttpStatus.OK, "Wishlist deleted successfully");
    }

    //Move to wishlistController
    @PutMapping("/update-wishlist")
    public ResponseEntity<Wishlist> updateWishlist(@RequestParam("wishlistId") Long wishlistId,
                                                   @RequestParam("wishlistName") String name,
                                                   @RequestParam("isPrivate") Boolean isPrivate) {
        Wishlist updatedWishlist = wishlistService.updateWishlist(wishlistId, name, isPrivate);
        return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AppUser user) throws UserNotFoundException, EmailNotFoundException, UsernameExistException, MessagingException {
        AppUser newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AppUser> login(@RequestBody AppUser user) {
        authenticate(user.getUsername(), user.getPassword());
        AppUser loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return ResponseEntity.ok().headers(jwtHeader).body(loginUser);
    }

    @PostMapping("/add")
    public ResponseEntity<AppUser> addNewUser(@RequestParam("firstName") String firstName,
                                              @RequestParam("lastName") String lastName,
                                              @RequestParam("username") String username,
                                              @RequestParam("email") String email,
                                              @RequestParam("role") String role,
                                              @RequestParam("isActive") String isActive,
                                              @RequestParam("isNonLocked") String isNonLocked,
                                              @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, EmailNotFoundException, IOException, UsernameExistException {
        AppUser user = userService.addNewUser(firstName, lastName, username, email, role,
                Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<AppUser> update(@RequestParam("currentUsername") String currentUsername,
                                          @RequestParam("firstName") String firstName,
                                          @RequestParam("lastName") String lastName,
                                          @RequestParam("username") String username,
                                          @RequestParam("email") String email,
                                          @RequestParam("role") String role,
                                          @RequestParam("isActive") String isActive,
                                          @RequestParam("isNonLocked") String isNonLocked,
                                          @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, EmailNotFoundException, IOException, UsernameExistException {
        AppUser updatedUser = userService.updateUser(currentUsername, firstName, lastName, username, email, role,
                Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    //Use @requestbody
    @PostMapping("/update/{id}")
    public ResponseEntity<AppUser> updateById(@PathVariable Long id,
                                              @RequestParam("firstName") String firstName,
                                              @RequestParam("lastName") String lastName,
                                              @RequestParam("username") String username,
                                              @RequestParam("email") String email,
                                              @RequestParam("role") String role,
                                              @RequestParam("isActive") String isActive,
                                              @RequestParam("isNonLocked") String isNonLocked,
                                              @RequestParam(value = "profileImage", required = false) MultipartFile profileImage)
            throws UserNotFoundException, EmailNotFoundException, IOException, UsernameExistException {
        AppUser updatedUser = userService.updateUserById(id, firstName, lastName, username, email, role,
                Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
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
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return response(HttpStatus.OK, USER_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<AppUser> updateProfileImage(@RequestParam("username") String username,
                                                      @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, EmailNotFoundException, IOException, UsernameExistException {
        AppUser user = userService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/image/{username}/{filename}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("filename") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER + username + FileConstant.FORWARD_SLASH + fileName));
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

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, tokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
