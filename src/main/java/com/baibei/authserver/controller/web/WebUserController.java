package com.baibei.authserver.controller.web;

import com.baibei.authserver.config.AppConfig;
import com.baibei.authserver.dto.UserDto;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.mapper.UserMapper;
import com.baibei.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class WebUserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AppConfig.Locale locale;

    @GetMapping
    public String profilePage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        UserDto userDto = userMapper.toDto(user);
        model.addAttribute("userDto", userDto);

        return switch (locale) {
            case RU -> "ru/profile";
            case EN -> "en/profile";
            default -> "en/profile";
        };
    }

    @PostMapping
    public String updateProfile(@ModelAttribute UserDto userDto,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        String authUsername = authentication.getName();

        if (!authUsername.equals(userDto.getUsername())) {
            redirectAttributes.addAttribute(
                    "errorMessage",
                    "You can only edit your own profile");
            return "redirect:/profile";
        }

        User user = userService.findByUsername(authUsername);

        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userService.save(user);

        redirectAttributes.addAttribute(
                "successMessage",
                "Profile updated successfully");
        redirectAttributes.addAttribute(
                "userDto",
                userMapper.toDto(user));

        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteAccount(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        userService.delete(user);
        return "redirect:/login?accountDeleted";
    }
}
