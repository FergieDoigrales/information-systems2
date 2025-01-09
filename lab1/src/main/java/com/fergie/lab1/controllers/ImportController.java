package com.fergie.lab1.controllers;

import com.fergie.lab1.models.ImportAudit;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.MovieAudit;
import com.fergie.lab1.models.RoleRequest;
import com.fergie.lab1.security.CustomUserDetails;
import com.fergie.lab1.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

@Controller
@RequestMapping("/import")
public class ImportController {

    private final ImportService importService;
    private final ImportAuditService importAuditService;

    @Autowired
    public ImportController(ImportService importService, ImportAuditService importAuditService) {
        this.importService = importService;
        this.importAuditService = importAuditService;
    }

    @GetMapping("")
    public String showImportPage(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "15") int size,
                                 @RequestParam(defaultValue = "id") String sort) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        model.addAttribute("userRole", userDetails.getRole().name());

        Pageable pageable = PageRequest.of(page, size);
        Page<ImportAudit> audit = importAuditService.findAll(pageable);
        model.addAttribute("currentPage", page);
        model.addAttribute("AuditPage", audit);
        return "import";
    }

    @PostMapping("/importMovies")
    public ResponseEntity<?> importFile(@RequestParam("file") MultipartFile file) throws NoSuchAlgorithmException, IOException {
        Long userId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        String fileHash = calculateHash(file);
        try {
            importService.importFile(file, userId, fileHash);
            return ResponseEntity.ok(new HashMap<String, String>() {{put("message", "Imported successfully");}});
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<String, String>() {{
                put("error", "Errors: " + e.getMessage());
            }});
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
