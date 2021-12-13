package com.company.atadu.elibrary.service;

import com.company.atadu.elibrary.dto.WishlistDto;
import com.company.atadu.elibrary.model.ElectronicFile;
import com.company.atadu.elibrary.model.AppUser;
import com.company.atadu.elibrary.model.Wishlist;
import com.company.atadu.elibrary.repo.ElectronicFileRepo;
import com.company.atadu.elibrary.repo.AppUserRepo;
import com.company.atadu.elibrary.repo.WishlistRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WishlistService {

    private final AppUserRepo appUserRepo;
    private final WishlistRepo wishlistRepo;
    private final ElectronicFileRepo electronicFileRepo;

    @Autowired
    public WishlistService(WishlistRepo wishlistRepo,
                           AppUserRepo appUserRepo,
                           ElectronicFileRepo electronicFileRepo) {
        this.appUserRepo = appUserRepo;
        this.wishlistRepo = wishlistRepo;
        this.electronicFileRepo = electronicFileRepo;
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
        Set<ElectronicFile> efileSet = wishlist.getElectronicFiles();
        efileSet.add(electronicFileRepo.getById(efileId));
        wishlist.setElectronicFiles(efileSet);
        wishlistRepo.save(wishlist);
    }

    public void removeFromWishlist(Long wishlistId, Long fileId) {
        Wishlist wishlist = wishlistRepo.getById(wishlistId);
        Set<ElectronicFile> files = wishlist.getElectronicFiles();
        files.remove(electronicFileRepo.getById(fileId));
        wishlist.setElectronicFiles(files);
        wishlistRepo.save(wishlist);
    }

    public Wishlist updateWishlist(WishlistDto wishlistDto) {
        Wishlist wishlist = wishlistRepo.getById(wishlistDto.getId());
        wishlist.setPrivate(wishlistDto.isPrivate());
        wishlist.setName(wishlistDto.getUserName());
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
