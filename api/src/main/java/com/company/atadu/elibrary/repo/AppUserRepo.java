package com.company.atadu.elibrary.repo;

import com.company.atadu.elibrary.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser findUserByUsername(String username);

    AppUser findUserByEmail(String email);
}
