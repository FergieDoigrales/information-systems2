package com.fergie.lab1.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fergie.lab1.security.JWTUtil;
import com.fergie.lab1.security.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JWTFilter(JWTUtil jwtUtil, UserDetailService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, java.io.IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (token.isBlank()) { //проверка наличия токена в хедере
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT in Bearer Header");
            } else {
                try { //проверка токена на валидность и загрузка пользователя
                    String username = jwtUtil.validateTokenAndRetrieveClaim(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (JWTVerificationException e){
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
            }
        }
    }
        filterChain.doFilter(httpServletRequest, httpServletResponse); //передача запроса дальше
    }
}
