package com.userservice.controller;

import com.userservice.model.Token;
import com.userservice.model.dto.request.UserLoginRequest;
import com.userservice.model.dto.request.UserRegisterRequest;
import com.userservice.service.RegisterService;
import com.userservice.service.UserLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final RegisterService registerService;
    private final UserLoginService userLoginService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Validated final UserRegisterRequest request) {
        registerService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody @Valid final UserLoginRequest request) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userLoginService.login(request));
    }
}
