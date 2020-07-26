package com.suprateam.car.service;


import com.suprateam.car.dto.UserDto;
import com.suprateam.car.model.SmeUser;

import java.util.List;

public interface UserServiceAuth {

    SmeUser save(UserDto user);
    List<SmeUser> findAll();
    void delete(long id);
    SmeUser findOne(String username);

    SmeUser findById(Long id);

    List<UserDto> getAllUser();

    String deleteUser(Long id);
}
