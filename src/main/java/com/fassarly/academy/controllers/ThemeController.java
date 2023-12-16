package com.fassarly.academy.controllers;

import com.fassarly.academy.entities.Theme;
import com.fassarly.academy.services.ThemeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theme")
@AllArgsConstructor
public class ThemeController {

    ThemeServiceImpl themeService;

    //-----------------------------------CRUD begins-----------------------------------//

    // Create Theme
    @PostMapping("/addTheme")
    public ResponseEntity<String> createTheme(@RequestBody Theme theme) {
        try {
            Theme createdTheme = themeService.createTheme(theme);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Theme créé avec succès\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite\"}");
        }
    }

    // Read All Themes
    @GetMapping("/getAllThemes")
    public ResponseEntity<?> readAllThemes() {
        try {
            List<Theme> allThemes = themeService.readAllTheme();
            return ResponseEntity.ok(allThemes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Read Theme by ID
    @GetMapping("/getTheme/{id}")
    public ResponseEntity<?> readTheme(@PathVariable Long id) {
        try {
            Theme theme = themeService.readTheme(id);
            if (theme != null) {
                return ResponseEntity.ok(theme);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Theme found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Update Theme
    @PutMapping("/updateTheme")
    public ResponseEntity<String> updateTheme(@RequestBody Theme theme) {
        try {
            Theme updatedTheme = themeService.updateTheme(theme);
            if (updatedTheme != null) {
                return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Theme mis à jour avec succès\"}");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Theme found.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    // Delete Theme by ID
    @DeleteMapping("/removeTheme/{id}")
    public ResponseEntity<String> deleteTheme(@PathVariable Long id) {
        try {
            themeService.deleteTheme(id);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Theme supprimé avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }

    //-----------------------------------CRUD ends-----------------------------------//

    //------------Rechercher Theme par nom------------//
    @GetMapping("/getThemeByNom/{nomTheme}")
    public ResponseEntity<?> readThemeByNom(@PathVariable String nomTheme) {
        try {
            Theme theme = themeService.findByNomTheme(nomTheme);
            if (theme != null) {
                return ResponseEntity.ok(theme);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"No Theme found for the given nomTheme.\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body("{\"error\": \"Une erreur s'est produite.\"}");
        }
    }
}
