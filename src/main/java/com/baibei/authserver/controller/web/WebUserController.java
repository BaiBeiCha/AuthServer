package com.baibei.authserver.controller.web;

import com.baibei.authserver.dto.UserDto;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.mapper.UserMapper;
import com.baibei.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class WebUserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @GetMapping
    public String profilePage(Authentication authentication, Model model) {
        String username = authentication.getName();
        System.out.println(username);
        User user = userService.findByUsername(username);
        UserDto userDto = userMapper.toDto(user);
        model.addAttribute("userDto", userDto);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute UserDto userDto,
                                Authentication authentication,
                                Model model) {
        String authUsername = authentication.getName();

        if (!authUsername.equals(userDto.getUsername())) {
            model.addAttribute("errorMessage", "You can only edit your own profile");
            return "profile";
        }

        User user = userService.findByUsername(authUsername);

        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userService.save(user);

        model.addAttribute("successMessage", "Profile updated successfully");
        model.addAttribute("userDto", userMapper.toDto(user));

        return "profile";
    }

    @PostMapping("/delete")
    public String deleteAccount(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        userService.delete(user);

        return "redirect:/login?accountDeleted";
    }
}
