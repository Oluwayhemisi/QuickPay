package com.example.payment.service;

import com.example.payment.entity.Payment;
import com.example.payment.payload.CardDto;
import com.example.payment.payload.PaymentDto;
import com.example.payment.payload.PaymentResponse;
import com.example.payment.payload.ValidateDto;


import java.net.UnknownHostException;
import java.util.List;

public interface PaymentService {

    PaymentResponse makePayment(CardDto cardDto, PaymentDto paymentDto, Long id) throws UnknownHostException;

    String validateCredentials(ValidateDto validateDto);

    List<Payment> getPayments(Long userId);
}
