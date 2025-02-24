package com.maghrebia.complaint.controller;

import com.maghrebia.complaint.entity.Complaint;
import com.maghrebia.complaint.entity.User;
import com.maghrebia.complaint.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RequestMapping("/api/v1/user")
@RestController
@RequiredArgsConstructor
public class userController {
  private final UserRepository userService;
    @PostMapping("/add")
    public ResponseEntity<?> adduser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }
}
