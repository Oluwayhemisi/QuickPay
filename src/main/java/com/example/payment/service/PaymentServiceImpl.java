package com.example.payment.service;

import com.example.payment.Enum.PaymentMethod;
import com.example.payment.Enum.Status;
import com.example.payment.entity.Payment;
import com.example.payment.entity.User;
import com.example.payment.exceptions.UserException;
import com.example.payment.payload.CardDto;
import com.example.payment.payload.PaymentDto;
import com.example.payment.payload.PaymentResponse;
import com.example.payment.payload.ValidateDto;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.repository.UserRepository;
import com.flutterwave.rave.java.entry.cardPayment;
import com.flutterwave.rave.java.entry.validateCardCharge;
import com.flutterwave.rave.java.payload.cardLoad;
import com.flutterwave.rave.java.payload.validateCardPayload;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.List;


@Service
@NoArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final String PUBLIC_KEY = System.getenv("PUBLIC_KEY");
    private final String ENCRYPTION_KEY = System.getenv("ENCRYPTION_KEY");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;



    @Override
    public PaymentResponse makePayment(CardDto cardDto, PaymentDto paymentDto, Long userId) throws UnknownHostException {
//        if (paymentDto.getPaymentMethod() != PaymentMethod.CARD) {
//            throw new PaymentException("Invalid payment method. Only CARD payments are supported.",HttpStatus.BAD_REQUEST);
//        }

        cardPayment cardPayment = new cardPayment();
        cardLoad cardload = new cardLoad();
        cardload.setPublic_key(PUBLIC_KEY);
        cardload.setCardno(cardDto.getCardNumber());
        cardload.setCvv(cardDto.getCvv());
        cardload.setExpirymonth(cardDto.getExpiryMonth());
        cardload.setCurrency("NGN");
        cardload.setCountry(cardDto.getCountry());
        cardload.setAmount(cardDto.getAmount());
        cardload.setEmail(cardDto.getEmail());
        cardload.setPhonenumber(cardDto.getPhoneNumber());
        cardload.setExpiryyear(cardDto.getExpiryYear());
        cardload.setEncryption_key(ENCRYPTION_KEY);
        cardload.setTxRef(generateId());


        String response = cardPayment.doflwcardpayment(cardload);


        JSONObject myObject = new JSONObject(response);

        String transaction_reference = "";

        if(myObject.optString("suggested_auth").equals("PIN")) {
            cardload.setPin(cardDto.getPin());


            cardload.setSuggested_auth("PIN");
            String response_one = cardPayment.doflwcardpayment(cardload);


            JSONObject iObject = new JSONObject(response_one);
            JSONObject Object = iObject.optJSONObject("data");

             transaction_reference = Object.optString("flwRef");

        }


        Payment payment = new Payment();
        payment.setAmount(cardDto.getAmount());
        payment.setPaymentMethod(PaymentMethod.CARD);
        payment.setCurrency("NGN");
        payment.setTransaction_reference(transaction_reference);
        payment.setStatus(Status.SUCCESSFUL);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found", HttpStatus.NOT_FOUND));
        payment.setUser(user);

        Payment savedPayment = paymentRepository.save(payment);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPayment(savedPayment);
        paymentResponse.setTransactionReference(transaction_reference);
        return paymentResponse;
    }

    @Override
    public String validateCredentials(ValidateDto validateDto) {

        validateCardCharge validatecardcharge = new validateCardCharge();
        validateCardPayload validatecardpayload = new validateCardPayload();
        validatecardpayload.setPBFPubKey(PUBLIC_KEY);
        validatecardpayload.setTransaction_reference(validateDto.getTransactionReference());
        validatecardpayload.setOtp(validateDto.getOtp());


        String response = validatecardcharge.doflwcardvalidate(validatecardpayload);

        return response;
    }

    public List<Payment> getPayments(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    private String generateId(){
        SecureRandom secureRandom = new SecureRandom();

        long minValue = 10000L;
        long maxValue = 99999L;

        long randomNumber = secureRandom.nextLong(minValue, maxValue + 1);

        return "RF" + randomNumber;
    }




}
