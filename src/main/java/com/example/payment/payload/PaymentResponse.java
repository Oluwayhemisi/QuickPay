package com.example.payment.payload;

import com.example.payment.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentResponse {

    private Payment payment;
    private String transactionReference;
}
