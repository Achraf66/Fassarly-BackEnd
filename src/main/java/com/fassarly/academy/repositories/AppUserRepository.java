package com.fassarly.academy.repositories;

import com.fassarly.academy.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {


    Optional<AppUser> findByNumeroTel(String numTel);

    List<AppUser> findByNomPrenomContainingIgnoreCaseOrNumeroTelContainingIgnoreCaseOrRolesDisplayNameContainingIgnoreCase(String nomPrenom, String numeroTel, String displayName);

}
