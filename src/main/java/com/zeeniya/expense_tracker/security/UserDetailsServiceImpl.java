package com.zeeniya.expense_tracker.security;

import org.springframework.stereotype.Service;
import com.zeeniya.expense_tracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
       return userRepository.findByEmail(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

}
