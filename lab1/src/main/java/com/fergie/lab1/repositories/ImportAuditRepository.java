package com.fergie.lab1.repositories;

import com.fergie.lab1.models.ImportAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportAuditRepository extends JpaRepository<ImportAudit, Long> {
    Optional<ImportAudit> findByFileHash(String hash);

    Optional<ImportAudit> findFirstByFileHashAndAuthorID(String hash, Long userId);

    Page<ImportAudit> findAllByAuthorID(Pageable pageable, Long userId);
}
