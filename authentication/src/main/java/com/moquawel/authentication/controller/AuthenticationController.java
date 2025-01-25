package com.moquawel.authentication.controller;



import com.moquawel.authentication.service.AuthenticationService;
import com.moquawel.authentication.request.AuthenticationRequest;
import com.moquawel.authentication.request.RefreshTokenRequest;
import com.moquawel.authentication.request.RegisterRequest;
import com.moquawel.authentication.response.AuthenticationResponse;
import com.moquawel.authentication.response.RefreshResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RefreshResponse> register(
            @RequestBody @Valid RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> login(
            @RequestBody @Valid RefreshTokenRequest request
    ){
        return ResponseEntity.ok(authenticationService.refresh(request));
    }


}
