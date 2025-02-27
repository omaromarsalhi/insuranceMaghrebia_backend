package com.maghrebia.user.service;

import com.maghrebia.user.entity.Role;
import com.maghrebia.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public ResponseEntity<Role> addRole(Role role) {
        return ResponseEntity.ok(roleRepository.save(role));
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
