package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Role;
import com.fassarly.academy.services.RoleServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
public class RoleController {

    RoleServiceImpl roleService;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create Role
    @PostMapping("/addRole")
    public ResponseEntity<String> createRole(@RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Role créé avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All Roles
    @GetMapping("/getAllRoles")
    public ResponseEntity<?> readAllRoles() {
        try {
            List<Role> allRoles = roleService.readAllRole();
            return ResponseEntity.ok(allRoles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read Role by ID
    @GetMapping("/getRole/{id}")
    public ResponseEntity<?> readRole(@PathVariable Long id) {
        try {
            Role role = roleService.readRole(id);
            if (role != null) {
                return ResponseEntity.ok(role);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Role found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Update Role
    @PutMapping("/updateRole")
    public ResponseEntity<String> updateRole(@RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(role);
            if (updatedRole != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Role mis à jour avec succès\"}");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Role found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete Role by ID
    @DeleteMapping("/removeRole/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Role supprimé avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    //-----------------------------------CRUD ends-----------------------------------//
}

