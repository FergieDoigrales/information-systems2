package com.fergie.lab1.repositories;

import com.fergie.lab1.models.ImportAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportAuditRepository extends JpaRepository<ImportAudit, Long> {
    Optional<ImportAudit> findByFileHash(String hash);
}
