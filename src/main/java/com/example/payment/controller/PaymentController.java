package com.example.payment.controller;

import com.example.payment.payload.*;
import com.example.payment.service.PaymentService;
import com.example.payment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;


@RestController
@RequestMapping("/api/makepayment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{id}")
    public ResponseEntity<PaymentResponse> makePayment(@RequestBody CardDto cardDto, PaymentDto paymentDto, @PathVariable("id") Long id) throws UnknownHostException {
        return new ResponseEntity<>(paymentService.makePayment(cardDto,paymentDto,id), HttpStatus.CREATED);


    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateCredentials(@RequestBody ValidateDto validateDto)   {
        return new ResponseEntity<>(paymentService.validateCredentials(validateDto), HttpStatus.CREATED);


    }
    @GetMapping("getpayments/{id}")
    public ResponseEntity<?> getPayments(@PathVariable("id") Long id)   {
        return new ResponseEntity<>(paymentService.getPayments(id), HttpStatus.FOUND);


    }
}
