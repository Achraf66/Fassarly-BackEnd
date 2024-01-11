package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.UserRole;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    //-----------------------------------CRUD begins-----------------------------------//
    public UserRole createRole(UserRole role);

    public List<UserRole> readAllRole();

    public UserRole readRole(Long id);

    public UserRole updateRole(UserRole role);

    public void  deleteRole(Long id);
    //-----------------------------------CRUD ends-----------------------------------//

    public Optional<UserRole> findbyid(Long idrole);
}
