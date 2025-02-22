package com.maghrebia.user.controller;

import com.maghrebia.user.dto.request.EmailRequest;
import com.maghrebia.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
