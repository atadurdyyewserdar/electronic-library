package com.company.atadu.elibrary.repo.resource;

import com.company.atadu.elibrary.model.efile.Efile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileResourceRepo extends JpaRepository<Efile, Long> {
}
