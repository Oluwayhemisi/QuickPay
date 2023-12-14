package com.example.payment.payload;

import com.example.payment.Enum.PaymentMethod;
import com.example.payment.Enum.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDto {

    private Long id;

    private String amount;
    private String currency;

    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    private String transaction_reference;

    private Status status;
}
