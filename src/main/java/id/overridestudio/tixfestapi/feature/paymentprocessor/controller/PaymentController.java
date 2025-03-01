package id.overridestudio.tixfestapi.feature.paymentprocessor.controller;

import com.midtrans.httpclient.error.MidtransError;
import id.overridestudio.tixfestapi.feature.paymentprocessor.dto.request.midtrans.PaymentRequest;
import id.overridestudio.tixfestapi.feature.paymentprocessor.dto.response.PaymentResponse;
import id.overridestudio.tixfestapi.feature.paymentprocessor.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(path = "api/payment/bank-transfer")
    public ResponseEntity<?> charge(@RequestBody PaymentRequest paymentRequest) throws MidtransError {
        PaymentResponse charge = paymentService.charge(paymentRequest);
        return new ResponseEntity<>(charge, HttpStatus.OK);
    }
}
