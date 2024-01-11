package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.UserRole;
import com.fassarly.academy.services.RoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class RoleController {


    RoleServiceImpl roleService;


    @GetMapping("/getAllRoles")
    public ResponseEntity<?> getAllRoles() {
        try {
            List<UserRole> allroles = roleService.readAllRole();
            return ResponseEntity.ok(allroles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }
}
