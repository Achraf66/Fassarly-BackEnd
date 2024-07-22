package com.fassarly.academy.controllers;


import com.fassarly.academy.DTO.AuthenticationResponse;
import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.services.ClasseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/classe")
@AllArgsConstructor
public class ClassesController {

    ClasseService ClasseService;

    @GetMapping("/students/by-class")
    public List<AppUser> getUsersByRole(@RequestParam String roleName) {
        return ClasseService.findUsersByRole(roleName);
    }
    @PostMapping("/deactivate")
    public ResponseEntity<AuthenticationResponse> deactivateUsers() {
        try {
            return  ResponseEntity.ok(ClasseService.desactivateUsers());
        } catch (Exception e) {
            return ResponseEntity.ok(AuthenticationResponse.builder().errormessage("error has occured").build());
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<AuthenticationResponse> activateUsers() {
        try {
            return  ResponseEntity.ok(ClasseService.allAccountsActivate());
        } catch (Exception e) {
            return ResponseEntity.ok(AuthenticationResponse.builder().errormessage("error has occured").build());
        }
    }



}
