package com.localexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.localexchange.service.FileStorageService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175"})
public class UploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<String> urls = fileStorageService.storeFiles(files);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Resource file = fileStorageService.loadAsResource(filename);
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        
        // Determine content type based on file extension
        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG;
        } else if (filename.toLowerCase().endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG;
        } else if (filename.toLowerCase().endsWith(".gif")) {
            contentType = MediaType.IMAGE_GIF;
        } else if (filename.toLowerCase().endsWith(".webp")) {
            contentType = MediaType.parseMediaType("image/webp");
        }
        
        return ResponseEntity.ok()
                .contentType(Objects.requireNonNull(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encoded + "\"")
                .body(file);
    }
}
