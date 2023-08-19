package com.epam.authenticationservice.config;

import com.epam.authenticationservice.dao.UserRepository;
import com.epam.authenticationservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private static final String METHOD_INVOKED = "method {} invoked in CustomUserDetailsService class with username: {}";
    private static final String METHOD_EXITED = "method {} exited in CustomUserDetailsService class with username: {}";

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(METHOD_INVOKED,"loadUserByUsername",username);
        Optional<User> user = userRepository.findByUserName(username);
        UserDetails userFound = user.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with given username"));
        log.info(METHOD_EXITED,"loadUserByUsername",username);
        return userFound;
    }
}
