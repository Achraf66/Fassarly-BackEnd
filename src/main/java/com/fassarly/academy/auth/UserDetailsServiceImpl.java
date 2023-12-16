package com.fassarly.academy.auth;

import com.fassarly.academy.entities.AppUser;
import com.fassarly.academy.repositories.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String numtel) throws UsernameNotFoundException {

        log.debug("Entering in loadUserByUsername Method...");
        Optional<AppUser> user = userRepository.findByNumeroTel(numtel);
        if(!user.isPresent()){
            log.error("Username not found: " + numtel);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user.get());
    }
}