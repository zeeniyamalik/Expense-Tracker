package com.zeeniya.expense_tracker.service;

import com.zeeniya.expense_tracker.dto.UserLogInRequest;
import com.zeeniya.expense_tracker.dto.UserResponse;
import com.zeeniya.expense_tracker.dto.UserSignUpRequest;
import com.zeeniya.expense_tracker.entity.User;
import com.zeeniya.expense_tracker.entity.Role;
import com.zeeniya.expense_tracker.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UserResponse signUp(UserSignUpRequest request){
        String password = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getEmail(), request.getUsername(), request.getName(), password, Role.USER);
        User savedUser =  userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getUsername(), savedUser.getName());
    }

    public Authentication logIn(UserLogInRequest request){
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        return authenticationManager.authenticate(authRequest);
    }


}
