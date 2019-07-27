package com.staten.capstone.controllers;

import com.staten.capstone.models.User;
import com.staten.capstone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private UserDao userDao;

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

    @GetMapping(value = "account/{userId}")
    public String showProfile(Model model, Principal principal,
                              @PathVariable int userId) {
        User user = userDao.findByUsername(principal.getName());
        if (userId != user.getId()) {
            // doesn't belong to current user, shouldn't be able to view it
            return "redirect:/unauthorized";
        }
        model.addAttribute("title", "My Account");
        return "user/account";
    }

    @GetMapping(value = "unauthorized")
    public String unauthorized(Model model) {
        model.addAttribute("title", "Unauthorized");
        return "user/unauthorized";
    }


    @GetMapping(value = "invalidSession")
    public String invalidSession(Model model) {
        model.addAttribute("title", "Invalid Session");
        return "user/invalidSession";
    }

    @GetMapping(value = "expiredSession")
    public String expiredSession(Model model) {
        model.addAttribute("title", "Expired Session");
        return "user/expiredSession";
    }
}
