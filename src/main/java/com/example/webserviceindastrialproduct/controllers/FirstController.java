package com.example.webserviceindastrialproduct.controllers;

import com.example.webserviceindastrialproduct.models.Bucket;
import com.example.webserviceindastrialproduct.models.Product;
import com.example.webserviceindastrialproduct.models.Role;
import com.example.webserviceindastrialproduct.models.User;
import com.example.webserviceindastrialproduct.services.ProductService;
import com.example.webserviceindastrialproduct.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class FirstController {
    UserService userService;
    ProductService productService;

    @GetMapping("/registr")
    public String registr(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registr")
    public String registration(Model model, @ModelAttribute("user") User user, HttpServletRequest request){
        String repeatPassword = request.getParameter("repeatPassword");
        try{
        if(repeatPassword.equals(user.getPassword())) {
            model.addAttribute("repeat",user.getPassword());
            user.setRole(Role.USER);
            user.setBucket(new Bucket());
            userService.signUp(user);
            return "redirect:/auth";
        } else {
            model.addAttribute("errorMessage", "Пароли не совпадают");
            return "registration";
        }
        }catch (RuntimeException e){
            model.addAttribute("errorMessage", "Пользователь с таким именем уже существует");
            return "registration";
        }
    }

    @GetMapping("/auth")
    public String login() {
        return "authorization";
    }

}
