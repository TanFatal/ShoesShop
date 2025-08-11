package com.example.learningAPISpring.auth.services;
import com.example.learningAPISpring.auth.entities.Authority;
import com.example.learningAPISpring.auth.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    public List<Authority> getUserAuthority(){
        List<Authority> authorities = new ArrayList<>();


        Optional<Authority> authorityOpt = Optional.ofNullable(authorityRepository.findByRoleCode("ROLE_CUSTOMER"));

        if (authorityOpt.isPresent()) {
            authorities.add(authorityOpt.get());
        } else {
            // Log warning hoặc throw exception
            throw new RuntimeException("ROLE_CUSTOMER not found");
        }

        return authorities;
    }

    public Authority createAuthority(String role, String description){
        Authority authority= Authority.builder().roleCode(role).roleDescription(description).build();
        return authorityRepository.save(authority);
    }

    public Authority getCustomerAuthority(){
        return findByRoleCode("ROLE_CUSTOMER");
    }
    public Authority getAdminAuthority(){
        return findByRoleCode("ROLE_ADMIN");
    }
    public Authority getRoleByCode(String roleCode) {
        return authorityRepository.findByRoleCode(roleCode);
    }
    private Authority findByRoleCode(String roleCode) {
        return authorityRepository.findByRoleCode(roleCode);
    }
    public List<Authority> getCustomerAuthorities(){
        return List.of(getCustomerAuthority());
    }

    public List<Authority> getAdminAuthorities(){
        return List.of(getAdminAuthority());
    }


}
