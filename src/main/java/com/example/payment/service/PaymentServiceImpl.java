package com.example.payment.service;

import com.example.payment.Enum.PaymentMethod;
import com.example.payment.Enum.Status;
import com.example.payment.entity.Payment;
import com.example.payment.entity.User;
import com.example.payment.exceptions.PaymentException;
import com.example.payment.exceptions.UserException;
import com.example.payment.payload.CardDto;
import com.example.payment.payload.PaymentDto;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.repository.UserRepository;
import com.flutterwave.rave.java.entry.cardPayment;
import com.flutterwave.rave.java.entry.validateCardCharge;
import com.flutterwave.rave.java.payload.cardLoad;
import com.flutterwave.rave.java.payload.validateCardPayload;
import lombok.RequiredArgsConstructor;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.net.UnknownHostException;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;


    @Value("${public.key}")
    private String publicKey;


    @Override
    public String makePayment(CardDto cardDto, PaymentDto paymentDto, Long userId) throws UnknownHostException {
//        if (paymentDto.getPaymentMethod() != PaymentMethod.CARD) {
//            throw new PaymentException("Invalid payment method. Only CARD payments are supported.",HttpStatus.BAD_REQUEST);
//        }

        cardPayment cardPayment = new cardPayment();
        cardLoad cardload = new cardLoad();
        cardload.setPublic_key(cardDto.getPublicKey());
        cardload.setCardno(cardDto.getCardNumber());
        cardload.setCvv(cardDto.getCvv());
        cardload.setExpirymonth(cardDto.getExpiryMonth());
        cardload.setCurrency("NGN");
        cardload.setCountry(cardDto.getCountry());
        cardload.setAmount(cardDto.getAmount());
        cardload.setEmail(cardDto.getEmail());
        cardload.setPhonenumber(cardDto.getPhoneNumber());
        cardload.setExpiryyear(cardDto.getExpiryYear());
        cardload.setEncryption_key(cardDto.getEncryptionKey());


        String response = cardPayment.doflwcardpayment(cardload);

        JSONObject myObject = new JSONObject(response);

        String transaction_reference = "";

        if(myObject.optString("suggested_auth").equals("PIN")) {
            cardload.setPin(cardDto.getPIN());
            cardload.setSuggested_auth("PIN");
            String response_one = cardPayment.doflwcardpayment(cardload);

            JSONObject iObject = new JSONObject(response_one);
            JSONObject Object = iObject.optJSONObject("data");

             transaction_reference = Object.optString("flwRef");
        }


        Payment payment = new Payment();
        payment.setAmount(paymentDto.getAmount());
        payment.setPaymentMethod(PaymentMethod.CARD);
        payment.setCurrency("NGN");
        payment.setStatus(Status.PENDING);
        payment.setTransaction_reference(transaction_reference);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
        payment.setUser(user);

        paymentRepository.save(payment);
        return transaction_reference;
    }

    @Override
    public JSONObject validateCredentials(String transactionReference, String otp) {

        validateCardCharge validatecardcharge = new validateCardCharge();
        validateCardPayload validatecardpayload = new validateCardPayload();
        validatecardpayload.setPBFPubKey(publicKey);
        validatecardpayload.setTransaction_reference(transactionReference);
        validatecardpayload.setOtp(otp);

        String response = validatecardcharge.doflwcardvalidate(validatecardpayload);
        JSONObject myObject = new JSONObject(response);
        return myObject;
    }


}
