package id.overridestudio.tixfestapi.feature.paymentprocessor.service.impl;

import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import id.overridestudio.tixfestapi.feature.paymentprocessor.dto.request.midtrans.PaymentRequest;
import id.overridestudio.tixfestapi.feature.paymentprocessor.dto.response.EWalletActionsResponse;
import id.overridestudio.tixfestapi.feature.paymentprocessor.dto.response.PaymentResponse;
import id.overridestudio.tixfestapi.feature.paymentprocessor.dto.response.VANumbersResponse;
import id.overridestudio.tixfestapi.feature.paymentprocessor.service.PaymentService;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.Order;
import id.overridestudio.tixfestapi.feature.transactionprocessing.entity.OrderDetails;
import id.overridestudio.tixfestapi.feature.transactionprocessing.service.OrderService;
import id.overridestudio.tixfestapi.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChargePaymentServiceImpl implements PaymentService {

    private final MidtransCoreApi midtransCoreApi;
    private final OrderService orderService;

    @Override
    public PaymentResponse charge(PaymentRequest request) throws MidtransError {

        Map<String, Object> transactionDetails = buildTransactionDetails(request);
        Map<String, Object> customerDetails = buildCustomerDetails(request);
//        List<Map<String, Object>> itemDetails = buildItemDetails(request);
        Map<String, Object> paymentMethodDetails = buildPaymentMethodDetails(request);
        Map<String, Object> customExpiryTime = buildCustomExpiryTime();

        Map<String, Object> requestCharge = new HashMap<>();
        requestCharge.put("payment_type", request.getPaymentType());
        requestCharge.put("transaction_details", transactionDetails);
        requestCharge.put("customer_details", customerDetails);
//        requestCharge.put("item_details", itemDetails);
        requestCharge.put("custom_expiry", customExpiryTime);
        requestCharge.put(request.getPaymentType(), paymentMethodDetails);

        JSONObject chargeTransaction = midtransCoreApi.chargeTransaction(requestCharge);

        List<EWalletActionsResponse> walletActions = extractWalletActions(chargeTransaction);
        VANumbersResponse vaNumberResponse = extractVANumbers(chargeTransaction);

        return buildPaymentResponse(chargeTransaction, walletActions, vaNumberResponse);
    }

    private Map<String, Object> buildTransactionDetails(PaymentRequest request) {
        Order order = orderService.getById(request.getOrderId());
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", "fest-" + UUID.randomUUID().toString());
        transactionDetails.put("gross_amount", "100000");
        return transactionDetails;
    }

    private Map<String, Object> buildCustomerDetails(PaymentRequest request) {
        Order order = orderService.getById(request.getOrderId());
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("name", "Haryanda Alfitroh");
        customerDetails.put("email", "haryandaalfitroh@gmail.com");
        customerDetails.put("phone_number", "0895422791833");
        return customerDetails;
    }

    private List<Map<String, Object>> buildItemDetails(PaymentRequest request) {
        Order order = orderService.getById(request.getOrderId());

        List<OrderDetails> orderDetails = order.getOrderDetails();

        return orderDetails.stream()
                .map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id", item.getId()); // ID order detail
                    itemMap.put("name", item.getTicket().getTicketCategory()); // Nama tiket
                    itemMap.put("price", item.getTicket().getPrice()); // Harga tiket
                    itemMap.put("quantity", item.getQuantity()); // Jumlah tiket
                    return itemMap;
                })
                .toList();
    }

    private Map<String, Object> buildPaymentMethodDetails(PaymentRequest request) {
        Map<String, Object> paymentMethodDetails = new HashMap<>();
        switch (request.getPaymentType()) {
            case "bank_transfer":
                paymentMethodDetails.put("bank", request.getBank());
                break;
            case "gopay":
                paymentMethodDetails.put("enable_callback", true);
                break;
            case "shopeepay":
                paymentMethodDetails.put("callback_url", "https://your-callback-url.com");
                break;
            case "cstore":
                paymentMethodDetails.put("store", request.getStore());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payment method");
        }
        return paymentMethodDetails;
    }

    private Map<String, Object> buildCustomExpiryTime() {
        Map<String, Object> customExpiryTime = new HashMap<>();
        customExpiryTime.put("order_time", DateTimeUtil.getCurrentTimeFormatted("GMT+7"));
        customExpiryTime.put("expiry_duration", 30);
        customExpiryTime.put("unit", "minute");

        return customExpiryTime;
    }

    private List<EWalletActionsResponse> extractWalletActions(JSONObject chargeTransaction) {
        JSONArray actionsArray = chargeTransaction.optJSONArray("actions");
        List<EWalletActionsResponse> walletActions = new ArrayList<>();

        if (actionsArray != null) {
            for (int i = 0; i < actionsArray.length(); i++) {
                JSONObject actionObject = actionsArray.getJSONObject(i);
                walletActions.add(new EWalletActionsResponse(
                        actionObject.getString("name"),
                        actionObject.getString("url"),
                        actionObject.getString("method")
                ));
            }
        }
        return walletActions;
    }

    private VANumbersResponse extractVANumbers(JSONObject chargeTransaction) {
        JSONArray vaNumbers = chargeTransaction.optJSONArray("va_numbers");
        if (vaNumbers != null && !vaNumbers.isEmpty()) {
            JSONObject vaNumber = vaNumbers.getJSONObject(0);
            return new VANumbersResponse(vaNumber.getString("bank"), vaNumber.getString("va_number"));
        }
        return null;
    }

    private PaymentResponse buildPaymentResponse(JSONObject chargeTransaction, List<EWalletActionsResponse> walletActions, VANumbersResponse vaNumberResponse) {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .statusCode(chargeTransaction.getString("status_code"))
                .statusMessage(chargeTransaction.getString("status_message"))
                .paymentType(chargeTransaction.getString("payment_type"))
                .orderId(chargeTransaction.getString("order_id"))
                .paymentStatus(chargeTransaction.getString("transaction_status"))
                .paymentTime(chargeTransaction.getString("transaction_time"))
                .amount(chargeTransaction.getString("gross_amount"))
                .fraudStatus(chargeTransaction.getString("fraud_status"))
                .walletActions(walletActions)
                .bankTransfer(vaNumberResponse)
                .expiryTime(chargeTransaction.getString("expiry_time"))
                .build();

        if (paymentResponse.getPaymentTime().equalsIgnoreCase("cstore")) {
            paymentResponse.setStorePaymentCode(chargeTransaction.getString("payment_code"));
        }
        return paymentResponse;
    }
}
