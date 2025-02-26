package com.maghrebia.user.controller;

import com.maghrebia.user.dto.request.ChangePasswordRequest;
import com.maghrebia.user.dto.request.EmployeeRegistrationRequest;
import com.maghrebia.user.dto.request.UpdateProfileRequest;
import com.maghrebia.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRegistrationRequest employeeRegistrationRequest) {
        return ResponseEntity.ok(userService.createUser(employeeRegistrationRequest));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @PostMapping("/edit-profile/{id}")
    public ResponseEntity<?> editProfile(@RequestBody @Valid UpdateProfileRequest user, @PathVariable String id) throws MessagingException {
        return ResponseEntity.ok(userService.updateUserProfile(user, id));
    }

    @PostMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, @PathVariable String id) {
        return ResponseEntity.ok(userService.changeProfilePassword(changePasswordRequest, id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestParam String id) {
        return ResponseEntity.ok(userService.getUsers(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id, @RequestParam String deleterId) {
        return ResponseEntity.ok(userService.deleteUser(id,deleterId));
    }
    @PostMapping("/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable String id,@RequestParam String bannerId) {
        return ResponseEntity.ok(userService.banUserById(id,bannerId));
    }
    @PostMapping("/unban/{id}")
    public ResponseEntity<?> unbanUser(@PathVariable String id,@RequestParam String unBannerId) {
        return ResponseEntity.ok(userService.unBanUserById(id,unBannerId));
    }
}
