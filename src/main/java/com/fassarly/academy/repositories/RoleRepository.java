package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {

    Set<UserRole> findUserRoleByNameIn(Set<String> roleNames);


}
