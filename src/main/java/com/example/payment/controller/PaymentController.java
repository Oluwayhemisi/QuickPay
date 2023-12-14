package com.example.payment.controller;

import com.example.payment.payload.CardDto;
import com.example.payment.payload.PaymentDto;
import com.example.payment.payload.UserDto;
import com.example.payment.service.PaymentService;
import com.example.payment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;


@RestController
@RequestMapping("/api/makepayment/{id}")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CardDto cardDto, PaymentDto paymentDto, @PathVariable("id") Long id) throws UnknownHostException {
        return new ResponseEntity<>(paymentService.makePayment(cardDto,paymentDto,id), HttpStatus.CREATED);


    }
}
