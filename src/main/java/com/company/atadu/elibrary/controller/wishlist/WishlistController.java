package com.company.atadu.elibrary.controller.wishlist;

import com.company.atadu.elibrary.dto.wishlist.WishlistDto;
import com.company.atadu.elibrary.model.wishlist.Wishlist;
import com.company.atadu.elibrary.service.wishlist.WishlistService;
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
}
