package com.fassarly.academy.controllers;


import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.services.ClasseService;
import lombok.AllArgsConstructor;
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


}
