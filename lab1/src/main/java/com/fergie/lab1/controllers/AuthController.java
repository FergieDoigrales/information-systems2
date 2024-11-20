package com.fergie.lab1.controllers;
import com.fergie.lab1.dto.AuthenticationDTO;
import com.fergie.lab1.dto.MovieDTO;
import com.fergie.lab1.dto.UserDTO;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.User;
import com.fergie.lab1.security.JWTUtil;
import com.fergie.lab1.security.UserDetailService;
import com.fergie.lab1.util.UserValidator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailService userDetailService;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;




    @Autowired
    public AuthController(UserDetailService userDetailService, UserValidator userValidator,
                          JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.userDetailService = userDetailService;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public
    ResponseEntity<Map<String, String>> authenticate(@RequestBody AuthenticationDTO authenticationDTO) {
        Logger logger = LoggerFactory.getLogger(AuthController.class);
        try {
            logger.info("Attempting to authenticate user: {}", authenticationDTO.getUsername());
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(authInputToken);
            authenticationManager.authenticate(authInputToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authenticationDTO.getUsername());
            logger.info("Authentication successful for user: {}", authenticationDTO.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("redirectUrl", "/home");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", authenticationDTO.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid username or password"));
        }
    }


    @GetMapping("/register")
    public String registerPage(@ModelAttribute("user") User user) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult,
                               @RequestParam("confirmPassword") String confirmPassword, Model model) {

        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (!user.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "error.password", "Passwords don't match.");
        }

        if (bindingResult.hasErrors()){
            return "/auth/register"; //тут пробрасывать исключение и ловить его с помощью @ExceptionHandler и выбрасывать Json с ошибкой в каком поле
        }

        userDetailService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());
        model.addAttribute("token", token); //???
        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "/auth/login";
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
