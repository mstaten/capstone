package com.staten.capstone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    @GetMapping(value = "login")
    public String login(Model model, Principal principal) {
        // if already logged in, redirect to home
        if (principal != null) {
            return "redirect:/";
        }
        model.addAttribute("title", "Login");
        return "user/login";
    }

    @GetMapping(value = "login/error")
    public String loginError(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("error", "Bad credentials");
        return "user/login";
    }

}
