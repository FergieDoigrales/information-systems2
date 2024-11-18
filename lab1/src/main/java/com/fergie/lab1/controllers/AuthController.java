package com.fergie.lab1.controllers;
import com.fergie.lab1.dto.AuthenticationDTO;
import com.fergie.lab1.dto.MovieDTO;
import com.fergie.lab1.dto.UserDTO;
import com.fergie.lab1.models.Movie;
import com.fergie.lab1.models.User;
import com.fergie.lab1.security.JWTUtil;
import com.fergie.lab1.security.UserDetailService;
import com.fergie.lab1.util.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailService userDetailService;
    private final UserValidator userValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

//    private final AuthenticationManager authenticationManager;



    @Autowired
    public AuthController(UserDetailService userDetailService, UserValidator userValidator, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.userDetailService = userDetailService;
        this.userValidator = userValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
//        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginPage() {
//        System.out.println("ОТЛАДКAAAAAAAAAAA");
        return "auth/login";
    }

    @GetMapping("/userInfo")
    public String getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        System.out.println("Username: " + username);
        return "auth/login";
    }


//    @PostMapping("/login")
//    public String authenticate(@RequestParam("username") String username) {
////        User user = userDetailService.findByUsername(username);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        String token = jwtUtil.generateToken(username);
//        return token; //?? что возвращать тут
//    }
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

//    @PostMapping("/login") //зачем использовать и AuthenticationDTO и UserDTO
//    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
//        UsernamePasswordAuthenticationToken authInputToken =
//                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword());
//        try {
//            Authentication authentication = authenticationManager.authenticate(authInputToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String token = jwtUtil.generateToken(authenticationDTO.getUsername());
//            return Collections.singletonMap("token", token); //???
//        } catch (Exception e) {
//            return Collections.singletonMap("error", "Invalid username or password");
//        }
//    }

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

    //
}
