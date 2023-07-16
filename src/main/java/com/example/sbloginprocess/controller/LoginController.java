package com.example.sbloginprocess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.sbloginprocess.model.User;
import com.example.sbloginprocess.repository.UserRepository;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home() {
        return "login"; // Assuming "index" is the name of your home page view template
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userRepository.findById(username).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            model.addAttribute("username", username);
            return "welcome";
        } else {
            model.addAttribute("error", "Username/Password incorrect or user does not exist");
            return "login";
        }
    }

    @GetMapping("/welcome")
    public String showWelcomePage(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "welcome";
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        User existingUser = userRepository.findById(username).orElse(null);
        if (existingUser != null && existingUser.getPassword().equals(password)) {
            model.addAttribute("error", "Username exists, you cannot create it");
            return "register";
        } else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            userRepository.save(newUser);
            return "redirect:/login";
        }
    }
}
