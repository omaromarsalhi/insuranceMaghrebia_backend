package com.maghrebia.payment.service;

import com.maghrebia.payment.config.StripeConfig;
import com.maghrebia.payment.dto.PaymentIntentDto;
import com.maghrebia.payment.dto.PaymentIntentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class PaymentStripeService {

    private  final StripeConfig stripeConfig;

    public PaymentIntentResponse createPaymentIntent(PaymentIntentDto paymentIntentDto) throws StripeException {
        stripeConfig.initializeStripe();
        System.out.println(" payment Intent at the beginnig  ");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentIntentDto.getAmount());
        System.out.println("the total amount is "+paymentIntentDto.getAmount());
        params.put("currency", paymentIntentDto.getCurrency());

        List<String> paymentMethodTypes = List.of("card");
        params.put("payment_method_types", paymentMethodTypes);
        params.put("payment_method", paymentIntentDto.getPaymentMethodId());
        params.put("capture_method", "manual"); // Authorize but don't capture

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return new PaymentIntentResponse(paymentIntent.getClientSecret());
    }

    public PaymentIntent confirm (String id  ) throws StripeException {
        stripeConfig.initializeStripe();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        Map<String, Object> params= new HashMap<>();
        params.put("payment_method","pm_card_visa");
        paymentIntent.confirm(params);
        return paymentIntent;
    }

    public PaymentIntent cancel (String id  ) throws StripeException {
        stripeConfig.initializeStripe();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();
        return paymentIntent;
    }
}
