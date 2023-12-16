package com.example.payment.service;

import com.example.payment.payload.UserDto;
import com.example.payment.payload.UserResponse;

public interface UserService {
    UserResponse createUser(UserDto userDto);

}
