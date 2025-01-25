package com.moquawel.authentication.service;


import com.moquawel.authentication.exception.DataMismatchException;
import com.moquawel.authentication.request.ChangePasswordRequest;
import com.moquawel.authentication.user.User;
import com.moquawel.authentication.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new DataMismatchException("Wrong password");
        }
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new DataMismatchException("Passwords are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);
    }

//
//    public List<UserDto> getStudents(int grade) {
//        var students = userRepository.findByRoleAndGrade(ERole.STUDENT, grade);
//        var users = new ArrayList<UserDto>();
//
//        students.forEach(
//                user -> users.add(buildUserDto(user))
//        );
//
//        return users;
//    }
//
//
//    public UserDto buildUserDto(User user) {
//        return UserDto
//                .builder()
//                .idUser(user.getIdUser())
//                .email(user.getEmail())
//                .role(user.getRole().name())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .identifier(user.getIdentifier())
//                .idGrade(user.getIdgrade())
//                .build();
//    }
}
