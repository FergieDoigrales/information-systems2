package com.fergie.lab1.controllers;

import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final ImportAuditService importAuditService;
    private final MoviesService moviesService;
    private final PeopleService peopleService;
    private final CoordinatesService coordinatesService;

    @Autowired
    public ImportController(ImportAuditService importAuditService, MoviesService moviesService, PeopleService peopleService,
                            CoordinatesService coordinatesService) {
        this.importAuditService = importAuditService;
        this.moviesService = moviesService;
        this.peopleService = peopleService;
        this.coordinatesService = coordinatesService;

    }

    @PostMapping("/importMovies")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException, IOException {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        String fileHash = calculateHash(file);
        try {
            System.out.println("Importing file");
//            ImportAudit importAudit = MoviesService.importFile(file, userId, fileHash);
//            importAuditService.saveAudit(importAudit);
            return ResponseEntity.ok("Imported successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errors: " + e.getMessage());
        }
    }

    //дописать пагинацию для аудита
//    @GetMapping("/ImportAudit")
//    public ResponseEntity<?> getImportAudit() {
//        return ResponseEntity.ok(importAuditService.findAll());
//    }


    private String calculateHash(MultipartFile file) {
        return "тут будет хеш когда-нибудь...";
    }

}
