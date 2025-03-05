package com.maghrebia.claim.controller;

import com.maghrebia.claim.entity.User;
import com.maghrebia.claim.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    public void createUser() {
        User user = new User(null, "7amadi", "7amadi");
        userService.saveUser(user);
    }
}
