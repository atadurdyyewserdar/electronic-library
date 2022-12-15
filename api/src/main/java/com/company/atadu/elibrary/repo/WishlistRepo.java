package com.company.atadu.elibrary.repo;

import com.company.atadu.elibrary.model.AppUser;
import com.company.atadu.elibrary.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistRepo extends JpaRepository<Wishlist, Long> {
    @Query("select u from Wishlist u where u.appUser = ?1")
    List<Wishlist> findWishlistByAppUser(AppUser appUser);
}
