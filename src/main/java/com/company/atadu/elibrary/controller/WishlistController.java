package com.company.atadu.elibrary.controller;

import com.company.atadu.elibrary.dto.WishlistDto;
import com.company.atadu.elibrary.model.HttpResponse;
import com.company.atadu.elibrary.model.Wishlist;
import com.company.atadu.elibrary.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = {"/", "/user"})
@CrossOrigin(origins = "*")
public class WishlistController {

    private WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
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

    @PutMapping("/update-wishlist")
    public ResponseEntity<Wishlist> updateWishlist(WishlistDto wishlistDto) {
        Wishlist updatedWishlist = wishlistService.updateWishlist(wishlistDto);
        return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
    }

    @PostMapping("/create-wishlist-2")
    public ResponseEntity<String> newWishList(@RequestBody WishlistDto wishlistDto) {
        String wishlistName = wishlistService.createWishlist(wishlistDto);
        return ResponseEntity.ok().body(wishlistName);
    }

    @GetMapping("/list-wishlists")
    public ResponseEntity<List<Wishlist>> wishlistsList() {
        List<Wishlist> wishlists = wishlistService.getAllWishlists();
        return new ResponseEntity<>(wishlists, HttpStatus.OK);
    }

    @GetMapping("/my-wishlists/{username}")
    public ResponseEntity<List<String>> getUsersWishlist(@PathVariable String username) {
        List<String> wishlists = wishlistService.getAllWishlistForUser(username);
        return new ResponseEntity<>(wishlists, HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String s) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                s.toUpperCase()), httpStatus);
    }
}
