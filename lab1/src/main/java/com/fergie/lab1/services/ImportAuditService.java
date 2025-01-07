package com.fergie.lab1.services;

import com.fergie.lab1.models.ImportAudit;
import com.fergie.lab1.repositories.ImportAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ImportAuditService {
    private final ImportAuditRepository importAuditRepository;

    @Autowired
    public ImportAuditService(ImportAuditRepository importAuditRepository) {
        this.importAuditRepository = importAuditRepository;
    }

    @Transactional
    public void save(ImportAudit importAudit) {
        importAuditRepository.save(importAudit);
    }

    public Optional<ImportAudit> findByHash(String hash) {
        return importAuditRepository.findByFileHash(hash);
    }

    public Page<ImportAudit> findAll(Pageable pageable) {
        return importAuditRepository.findAll(pageable);
    }
}
