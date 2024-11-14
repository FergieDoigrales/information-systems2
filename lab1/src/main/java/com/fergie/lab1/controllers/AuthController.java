package com.fergie.lab1.controllers;

import com.fergie.lab1.models.User;
import com.fergie.lab1.security.JWTUtil;
import com.fergie.lab1.security.UserDetailService;
import com.fergie.lab1.util.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailService userDetailService;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;

    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(UserDetailService userDetailService, UserValidator userValidator, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.userDetailService = userDetailService;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public String loginPage() {
//        System.out.println("ОТЛАДКAAAAAAAAAAA");
        return "auth/login";
    }
//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationSucceedDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
//        User user = userDetailService.findByUsername(loginUserDto.getUsername());
//        if (user == null || !user.getPassword().equals(loginUserDto.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationSucceedDto(null));
//        }
//        String token = jwtUtil.generateToken(user.getUsername());
//        return ResponseEntity.ok(new AuthenticationSucceedDto(token));
//    }

    @GetMapping("/register")
    public String registerPage(@ModelAttribute("user") User user) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                               @RequestParam("confirmPassword") String confirmPassword, Model model) {
        userValidator.validate(user, bindingResult);

        if (!user.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "error.password", "Passwords don't match.");
        }

        if (bindingResult.hasErrors()){
            return "/auth/register";
        }

        userDetailService.register(user);
//        System.out.println("AAAAAAAAAAA");
        String token = jwtUtil.generateToken(user.getUsername());

        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "/auth/login";
    }

    //
}
