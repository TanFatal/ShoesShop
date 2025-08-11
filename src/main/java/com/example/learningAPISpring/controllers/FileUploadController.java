package com.example.learningAPISpring.controllers;

import com.example.learningAPISpring.service.FileUploadSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileUploadController {
    @Autowired
    private FileUploadSerive fileUploadSerive;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = fileUploadSerive.uploadFile(file);
        if (url != null) {
            return new ResponseEntity<>(url, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
