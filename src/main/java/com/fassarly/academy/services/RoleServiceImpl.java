package com.fassarly.academy.services;


import com.fassarly.academy.entities.UserRole;
import com.fassarly.academy.interfaceServices.IRoleService;
import com.fassarly.academy.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    //-----------------------------------CRUD begins-----------------------------------//
    @Override
    public UserRole createRole(UserRole role) {
        // Implementation to create a new role
        return roleRepository.save(role);
    }

    @Override
    public List<UserRole> readAllRole() {
        // Implementation to retrieve all roles
        return roleRepository.findAll();
    }

    @Override
    public UserRole readRole(Long id) {
        // Implementation to retrieve a role by ID
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public UserRole updateRole(UserRole role) {
        // Implementation to update a role
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        // Implementation to delete a role by ID
        roleRepository.deleteById(id);
    }
    //-----------------------------------CRUD ends-----------------------------------//

    @Override
    public Optional<UserRole> findbyid(Long idrole) {
        // Implementation to find a role by ID
        return roleRepository.findById(idrole);
    }

}
