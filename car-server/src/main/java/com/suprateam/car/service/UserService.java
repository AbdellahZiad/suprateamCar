package com.suprateam.car.service;

import com.suprateam.car.dto.UserDto;

public interface UserService {

    java.util.List<UserDto> getAllUser();

    String deleteUser(Long id);

    UserDto saveUser(UserDto userDto);
}
