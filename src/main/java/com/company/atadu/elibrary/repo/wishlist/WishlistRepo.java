package com.company.atadu.elibrary.repo.wishlist;

import com.company.atadu.elibrary.model.wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepo extends JpaRepository<Wishlist, Long> {
}
