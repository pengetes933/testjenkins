package id.overridestudio.tixfestapi.feature.paymentprocessor.service;

import com.midtrans.httpclient.error.MidtransError;
import id.overridestudio.tixfestapi.feature.paymentprocessor.dto.request.midtrans.PaymentRequest;
import id.overridestudio.tixfestapi.feature.paymentprocessor.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse charge(PaymentRequest request) throws MidtransError;
}
