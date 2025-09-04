package org.example.bank_rest_p_n.config;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.model.enumClass.MyRole;
import org.example.bank_rest_p_n.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${admin.password:qweqwe}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        addAdmin();
    }

    private void addAdmin() {
        String adminMail = "admin.main@mail.com";
        if (userRepository.findByEmailEqualsIgnoreCase(adminMail).isEmpty()) {
            MyUser admin = new MyUser();
            admin.setFirstName("MAIN-ADMIN");
            admin.setEmail(adminMail);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole(MyRole.ADMIN);
            userRepository.save(admin);
        }
    }
}