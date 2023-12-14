package com.example.payment.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardDto {

    @Value("${public.key}")
    private String publicKey;
    private String cardNumber;
    private String cvv;
    private String expiryMonth;
    private String currency;
    private String expiryYear;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String country;
    private String amount;
    private String PIN;

    @Value("${encryption.key}")
    private String encryptionKey;
}
