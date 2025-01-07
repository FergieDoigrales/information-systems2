package com.fergie.lab1.controllers;

import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final MoviesService moviesService;


    @Autowired
    public ImportController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @GetMapping("/page")
    public String showImportPage(Model model) {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return "import";
    }

    @PostMapping("/importMovies")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException, IOException {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        String fileHash = calculateHash(file);
        try {
            moviesService.importFile(file, userId, fileHash);
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


    private String calculateHash(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(file.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

}
