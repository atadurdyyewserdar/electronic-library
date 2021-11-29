package com.company.atadu.elibrary.service.wishlist;

import com.company.atadu.elibrary.dto.wishlist.WishlistDto;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    public String createWishlist(WishlistDto wishlistDto) {
        return "new wishlist";
    }

    public Long addNewItemToWishlist(Long id) {
        return null;
    }
}
