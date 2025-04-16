package com.maghrebia.user.controller;

import com.maghrebia.user.entity.Role;
import com.maghrebia.user.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("role")
@Tag(name = "Role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        return roleService.addRole(role);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAllRoles());
    }
}
