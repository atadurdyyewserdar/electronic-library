package com.company.atadu.elibrary.service.wishlist;

import com.company.atadu.elibrary.dto.wishlist.WishlistDto;
import com.company.atadu.elibrary.model.efile.Efile;
import com.company.atadu.elibrary.model.user.AppUser;
import com.company.atadu.elibrary.model.user.UserPrincipal;
import com.company.atadu.elibrary.model.wishlist.Wishlist;
import com.company.atadu.elibrary.repo.efile.EfileRepo;
import com.company.atadu.elibrary.repo.user.AppUserRepo;
import com.company.atadu.elibrary.repo.wishlist.WishlistRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WishlistService {

    private final AppUserRepo appUserRepo;
    private final WishlistRepo wishlistRepo;
    private final EfileRepo efileRepo;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public WishlistService(WishlistRepo wishlistRepo,
                           AppUserRepo appUserRepo,
                           EfileRepo efileRepo) {
        this.appUserRepo = appUserRepo;
        this.wishlistRepo = wishlistRepo;
        this.efileRepo = efileRepo;
    }

    public String createWishlist(WishlistDto wishlistDto) {
        AppUser appUser = appUserRepo.findUserByUsername(wishlistDto.getUserName());
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

    public List<Wishlist> getAllWishlists() {
        return wishlistRepo.findAll();
    }

    public List<String> getAllWishlistForUser(String username) {
        AppUser appUser = appUserRepo.findUserByUsername(username);
        List<Wishlist> wishlists = wishlistRepo.findWishlistByAppUser(appUser);
        return wishlists.stream().map(Wishlist::getName).collect(Collectors.toList());
    }
}
