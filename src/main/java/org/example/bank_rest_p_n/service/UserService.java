package org.example.bank_rest_p_n.service;

import ch.qos.logback.classic.encoder.JsonEncoder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bank_rest_p_n.exception.IllegalOperation;
import org.example.bank_rest_p_n.exception.NoDataFoundException;
import org.example.bank_rest_p_n.model.dto.AdminUpdateUserDTO;
import org.example.bank_rest_p_n.model.dto.FilterUserDTO;
import org.example.bank_rest_p_n.model.dto.MyUserRegisterDTO;
import org.example.bank_rest_p_n.model.dto.MyUserUpdateDTO;
import org.example.bank_rest_p_n.model.entity.MyUser;
import org.example.bank_rest_p_n.model.enumClass.MyRole;
import org.example.bank_rest_p_n.repository.UserRepository;
import org.example.bank_rest_p_n.repository.specification.UserSpecifications;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserService — [comment]
 *
 * @author Pavel Nenahov
 * @version 1.0
 * @since 03/09/2025
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${page.size.product:10}")
    private Integer PAGE_SIZE;
    private PasswordEncoder passwordEncoder;

    public Boolean blockUser(@Valid AdminUpdateUserDTO requestDTO) {
        MyUser user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(()-> new NoDataFoundException("User not found"));
        if(user.getIsBlocked().equals(requestDTO.getIsBlocked())) {
            throw new IllegalOperation("User is blocked");
        }
        user.setIsBlocked(requestDTO.getIsBlocked());
        userRepository.save(user);

        return true;
    }

    public Boolean deleteUser(@Valid AdminUpdateUserDTO requestDTO) {
        userRepository.deleteById(requestDTO.getUserId());
        return true;
    }

    public List<MyUser> getUsersByFilter(FilterUserDTO requestDTO, int pageNumber) {
        return userRepository.findAll(UserSpecifications.filter(requestDTO),
                PageRequest.of(pageNumber, PAGE_SIZE))
                .getContent();
    }

    public Boolean register(@Valid MyUserRegisterDTO dto) {
        MyUser myUser = new MyUser();
        myUser.setFirstName(dto.getFirstName());
        myUser.setLastName(dto.getLastName());
        myUser.setEmail(dto.getEmail());
        myUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        myUser.setRole(MyRole.USER);

        if(userRepository.existsByEmailEqualsIgnoreCase(dto.getEmail())){
            throw new IllegalOperation("Email already in use");
        }

        userRepository.save(myUser);
        return true;
    }

    public Boolean updateInfo(@NotNull MyUser user, @Valid MyUserUpdateDTO dto) {
        if(dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if(dto.getLastName() != null) user.setLastName(dto.getLastName());
        if(dto.getEmail() != null) user.setEmail(dto.getEmail());
        if(dto.getPassword() != null) user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        return true;
    }
}
