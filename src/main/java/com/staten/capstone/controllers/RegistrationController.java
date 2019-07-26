package com.staten.capstone.controllers;

import com.staten.capstone.errors.PasswordsMismatchException;
import com.staten.capstone.errors.UserAlreadyExistsException;
import com.staten.capstone.models.User;
import com.staten.capstone.models.data.UserDao;
import com.staten.capstone.models.data.UserDto;
import com.staten.capstone.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class RegistrationController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IUserService userService;

    @GetMapping(value = "register")
    public String register(Model model, Principal principal) {
        // if already logged in, redirect to home
        if (principal != null) {
            return "redirect:/";
        }
        model.addAttribute("title", "Register New User");
        model.addAttribute(new UserDto());
        return "user/register";
    }

    @PostMapping(value = "register")
    public String register(@ModelAttribute @Valid UserDto userDto,
                           Errors errors, Model model) {
        //if user input is invalid
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors("username")) {
                userDto.setUsername(""); // reset username
            }

            model.addAttribute("title", "Register New User");
            return "user/register";
        }

        User user;

        // try to register new user, catch if username already exists
        try {
            // might throw UserAlreadyExistsException
            user = userService.registerNewUser(userDto);
        } catch (UserAlreadyExistsException | PasswordsMismatchException ex) {

            // display register form again with this error
            model.addAttribute("title", "Register New User");
            model.addAttribute("ex", ex);
            return "user/register";
        }

        // save user
        userDao.save(user);
        return "redirect:";
    }
}
