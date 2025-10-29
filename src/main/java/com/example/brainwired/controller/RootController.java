package com.example.brainwired.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class RootController {

    /**
     * Redirects root to the Swagger UI page so that visiting http://localhost:8080/ opens the API UI.
     */
    @GetMapping("/")
    public ResponseEntity<Void> root() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/swagger-ui.html"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
