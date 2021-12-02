package com.company.atadu.elibrary.service.wishlist;

import com.company.atadu.elibrary.dto.wishlist.WishlistDto;
import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.user.AppUser;
import com.company.atadu.elibrary.model.user.UserPrincipal;
import com.company.atadu.elibrary.model.wishlist.Wishlist;
import com.company.atadu.elibrary.repo.efile.EfileRepo;
import com.company.atadu.elibrary.repo.user.AppUserRepo;
import com.company.atadu.elibrary.repo.wishlist.WishlistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WishlistService {

    private final AppUserRepo appUserRepo;
    private final WishlistRepo wishlistRepo;
    private final EfileRepo efileRepo;

    @Autowired
    public WishlistService(WishlistRepo wishlistRepo,
                           AppUserRepo appUserRepo,
                           EfileRepo efileRepo) {
        this.appUserRepo = appUserRepo;
        this.wishlistRepo = wishlistRepo;
        this.efileRepo = efileRepo;
    }

    public String createWishlist(WishlistDto wishlistDto) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser = appUserRepo.findUserByUsername(principal.getUsername());
        Wishlist wishlist = new Wishlist();
        wishlist.setAppUser(appUser);
        wishlist.setPrivate(wishlistDto.isPrivate());
        wishlist.setName(wishlistDto.getWishlistName());
        wishlistRepo.save(wishlist);
        return wishlist.getName();
    }

    public void addNewItemToWishlist(Long efileId, Long wishlistId) {
        Wishlist wishlist = wishlistRepo.getById(wishlistId);
        Set<Efile> efileSet = wishlist.getEfiles();
        efileSet.add(efileRepo.getById(efileId));
        wishlist.setEfiles(efileSet);
        wishlistRepo.save(wishlist);
    }

    public void removeFromWishlist(Long wishlistId, Long efileId) {
        Wishlist wishlist = wishlistRepo.getById(wishlistId);
        Set<Efile> efileSet = wishlist.getEfiles();
        efileSet.remove(efileRepo.getById(efileId));
        wishlist.setEfiles(efileSet);
        wishlistRepo.save(wishlist);
    }

    public Wishlist updateWishlist(Long wishlistId, String name, boolean isPrivate) {
        Wishlist wishlist = wishlistRepo.getById(wishlistId);
        wishlist.setPrivate(isPrivate);
        wishlist.setName(name);
        wishlistRepo.save(wishlist);
        return wishlist;
    }

    public void deleteWishlist(Long wishlistId) {
        wishlistRepo.deleteById(wishlistId);
    }
}
