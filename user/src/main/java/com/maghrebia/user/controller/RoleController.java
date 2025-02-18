package com.maghrebia.user.controller;

import com.maghrebia.user.entity.Role;
import com.maghrebia.user.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("role")
@Tag(name = "Authentication")
public class RoleController {
    private final RoleService roleService;
    @PostMapping("/add")
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        return roleService.addRole(role);
    }
}
