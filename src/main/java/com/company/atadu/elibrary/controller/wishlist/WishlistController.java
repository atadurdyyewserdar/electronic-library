package com.company.atadu.elibrary.controller.wishlist;

import com.company.atadu.elibrary.dto.wishlist.WishlistDto;
import com.company.atadu.elibrary.dto.wishlist.WishlistItem;
import com.company.atadu.elibrary.service.wishlist.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/create-wishlist")
    public ResponseEntity<String> newWishList(@RequestBody WishlistDto wishlistDto) {
        String wishlistName = wishlistService.createWishlist(wishlistDto);
        return ResponseEntity.ok().body(wishlistName);
    }

    @PostMapping("/add-item-wishlist")
    public ResponseEntity<String> addItemToWishlist(@RequestParam("id") Long id) {
        wishlistService.addNewItemToWishlist(id);
        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/list-items")
    public ResponseEntity<WishlistItem> getWishlistItems () {
        return null;
    }
}
