package org.example.email.controller;

import org.example.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Boolean> sendEmail() {
        emailService.sendEmail("deneme@deneme.com", "Selam", "Deneme");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
