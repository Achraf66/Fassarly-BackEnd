package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.Role;

import java.util.List;

public interface IRoleService {
    //-----------------------------------CRUD begins-----------------------------------//
    public Role createRole(Role role);

    public List<Role> readAllRole();

    public Role readRole(Long id);

    public Role updateRole(Role role);

    public void  deleteRole(Long id);
    //-----------------------------------CRUD ends-----------------------------------//
}
