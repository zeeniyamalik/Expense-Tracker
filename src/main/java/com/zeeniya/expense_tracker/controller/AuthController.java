package com.zeeniya.expense_tracker.controller;

import com.zeeniya.expense_tracker.dto.UserLogInRequest;
import com.zeeniya.expense_tracker.dto.UserResponse;
import com.zeeniya.expense_tracker.dto.UserSignUpRequest;
import com.zeeniya.expense_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> singUp(@Valid @RequestBody UserSignUpRequest request){
        UserResponse response = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@Valid @RequestBody UserLogInRequest request){
        String token = userService.logIn(request);
        return ResponseEntity.ok(token);
    }


}
