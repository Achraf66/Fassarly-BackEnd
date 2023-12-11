package com.fassarly.academy.services;

import com.fassarly.academy.entities.Role;
import com.fassarly.academy.interfaceServices.IRoleService;
import com.fassarly.academy.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class RoleServiceImpl implements IRoleService {

    RoleRepository roleRepository;
    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> readAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Role readRole(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
     roleRepository.deleteById(id);
    }
}
