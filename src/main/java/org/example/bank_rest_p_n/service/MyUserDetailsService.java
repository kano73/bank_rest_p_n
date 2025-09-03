package org.example.bank_rest_p_n.service;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.details.MyUserDetails;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository UserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MyUser myUser = UserRepository.findByEmailEqualsIgnoreCase(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + email));
        return new MyUserDetails(myUser);
    }
}