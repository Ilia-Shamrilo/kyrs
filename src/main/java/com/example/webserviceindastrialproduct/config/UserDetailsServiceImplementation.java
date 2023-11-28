package com.example.webserviceindastrialproduct.config;

import com.example.webserviceindastrialproduct.models.User;
import com.example.webserviceindastrialproduct.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImplementation implements UserDetailsService {
    UserService userService;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userService.findName(name).orElseThrow(()-> new UsernameNotFoundException("Неверный логин или пароль"));
        return UserDetailsImplementation.build(user);
    }
}