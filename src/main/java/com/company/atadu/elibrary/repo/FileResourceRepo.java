package com.company.atadu.elibrary.repo;

import com.company.atadu.elibrary.model.ElectronicFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileResourceRepo extends JpaRepository<ElectronicFile, Long> {
}
