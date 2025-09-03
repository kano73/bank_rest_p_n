package org.example.bank_rest_p_n.service.api;

import org.example.bank_rest_p_n.model.dto.*;
import org.example.bank_rest_p_n.model.entity.MyUser;

import java.util.List;

public interface UserService {
    MyUser findUserByEmail(String email);
    Boolean blockUser(AdminUpdateUserDTO requestDTO);
    Boolean deleteUser(AdminUpdateUserDTO requestDTO);
    List<MyUser> getUsersByFilter(FilterUserDTO requestDTO, int pageNumber);
    Boolean register(MyUserRegisterDTO dto);
    Boolean updateInfo(MyUser user, MyUserUpdateDTO dto);
}
