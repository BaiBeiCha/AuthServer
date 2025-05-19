package com.baibei.authserver.controller.web;

import com.baibei.authserver.config.AppConfig;
import com.baibei.authserver.dto.RegisterRequest;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.service.RoleService;
import com.baibei.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class WebAuthController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterRequest registerRequest, Model model) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            model.addAttribute("errorMessage", "Username is already taken");
            return "register";
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleService.findByName("USER"));
        userService.save(user);

        return "redirect:/login?registerSuccess";
    }
}
