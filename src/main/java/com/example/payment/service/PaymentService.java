package com.example.payment.service;

import com.example.payment.entity.Payment;
import com.example.payment.payload.CardDto;
import com.example.payment.payload.PaymentDto;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.net.UnknownHostException;

public interface PaymentService {

    String makePayment(CardDto cardDto, PaymentDto paymentDto, Long id) throws UnknownHostException;

    JSONObject validateCredentials(String transactionReference, String otp);
}
