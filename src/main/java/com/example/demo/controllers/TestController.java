package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*", maxAge = 3600)
    @RestController
    @RequestMapping("/api/ver1/test")
    public class TestController {
        @GetMapping("/all")
        public String allAccess() {
            return "Public Content.";
        }

        @GetMapping("/customer")
        @PreAuthorize("hasRole('Customer') or hasRole('Admin') or hasRole('Owner')")
        public String customerAccess() {
            return "Customer Content.";
        }

        @GetMapping("/owner")
        @PreAuthorize("hasRole('Owner')")
        public String ownerAccess() {
            return "Owner Board.";
        }

        @GetMapping("/admin")
        @PreAuthorize("hasRole('Admin')")
        public String adminAccess() {
            return "Admin Board.";
        }

        @GetMapping("/books")
        public ResponseEntity<Object[]> booksList() {
            RestTemplate restTemplate = new RestTemplate();
            String fooResourceUrl
                    = "http://localhost:8081/api/ver1/books/";
            ResponseEntity<Object[]> response
                    = restTemplate.getForEntity(fooResourceUrl,
                    Object[].class);
            return response;
        }

    //    public ResponseEntity<List<Role>> booksList() {
//        RestTemplate restTemplate = new RestTemplate();
//        String fooResourceUrl
//                = "http://localhost:8081/api/ver1/books/";
//        ResponseEntity<List<Role>> response
//                = restTemplate.exchange(fooResourceUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Role>>() {});
//        return response;
//    }

    /*public ResponseEntity<String> booksList() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8081/api/ver1/books/";
        ResponseEntity<String> response
               = restTemplate.exchange(fooResourceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {});
        return response;
    }*/
    }
