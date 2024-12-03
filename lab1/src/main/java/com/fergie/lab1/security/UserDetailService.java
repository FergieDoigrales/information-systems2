package com.fergie.lab1.security;

import com.fergie.lab1.models.User;
import com.fergie.lab1.models.enums.AccessRole;
import com.fergie.lab1.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class UserDetailService implements UserDetailsService {

    private final UsersRepository usersRepository;

    private PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder(); //переписать на переменную экземпляра вместо метода
    }

    @Autowired
    public UserDetailService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(user); //
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
    public User findById(Long userId){ //???
        Optional<User> user = usersRepository.findById(userId);
        return user.orElse(null);
    }

    public User findByUsername(String username){ //???
        Optional<User> user = usersRepository.findByUsername(username);
        return user.orElse(null);
    }

    public List<User> findAll(){ //???
        return usersRepository.findAll();
    }
    @Transactional
    public void register(User user) {
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setRole(AccessRole.USER); //или убрать отсюда, или из базового класса
//        user.setRole(user.getRole()); // добавить потом смену ролей
        usersRepository.save(user);
        System.out.println("User saved with ID: " + user.getUsername());
    }

    public void setAdminRole(String username) {
        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        user.setRole(AccessRole.ADMIN);
        usersRepository.save(user);
    }

    public CustomUserDetails getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }

}
