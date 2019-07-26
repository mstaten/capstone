package com.staten.capstone.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title", "Local Reports");
        return "index";
    }

    @GetMapping(value = "login")
    public String login(Model model) {
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
