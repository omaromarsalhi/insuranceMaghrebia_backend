package com.maghrebia.authentication.response;

import com.maghrebia.authentication.user.Role;

import java.util.ArrayList;

public record AuthenticationResponse(
        String token,
        String refreshToken,
        String firstname,
        String lastname,
        String email,
        ArrayList<Role> roles
) {
}
