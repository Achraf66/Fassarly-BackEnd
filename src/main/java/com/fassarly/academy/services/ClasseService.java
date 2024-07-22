package com.fassarly.academy.services;

import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClasseService {

    private final AppUserRepository appUserRepository;

    public List<AppUser> findUsersByRole(String roleName) {
        return appUserRepository.findByRolesNameContaining(roleName);
    }

}
