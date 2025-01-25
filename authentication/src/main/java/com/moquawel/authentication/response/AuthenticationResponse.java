package com.moquawel.authentication.response;

import com.moquawel.authentication.user.Role;

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
