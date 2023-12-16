package com.example.payment.service;

import com.example.payment.Enum.PaymentMethod;
import com.example.payment.Enum.Status;
import com.example.payment.entity.Payment;
import com.example.payment.entity.User;
import com.example.payment.exceptions.UserException;
import com.example.payment.payload.UserDto;
import com.example.payment.payload.UserResponse;
import com.example.payment.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isPresent()) {
            throw new UserException("User Already Exist", HttpStatus.BAD_REQUEST);
        }
        User user1 = new User();
        user1.setEmail(userDto.getEmail());
        user1.setPhoneNumber(userDto.getPhoneNumber());
        user1.setPassword(userDto.getPassword());

        User savedUser = userRepository.save(user1);
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPhoneNumber(savedUser.getPhoneNumber());
        userResponse.setId(savedUser.getId());

        return userResponse;

    }
}
