package com.xxgradzix.ServerManagementSystem.paypal.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.xxgradzix.ServerManagementSystem.paypal.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/paypal")
public class PayPalController {

    private final PayPalService payPalService;

    private final String cancelUrl = "http://localhost:8081/api/v1/paypal/payment/cancel";
    private final String successUrl = "http://localhost:8081/api/v1/paypal/payment/success";
    private final String errorUrl = "http://localhost:8081/api/v1/paypal/payment/error";

//    @PostMapping("/payment/create")
//    public RedirectView createPayment() {
//
//        try {
//            Payment payment = payPalService.createPayment(10.0, "USD", "paypal", "sale", "description", cancelUrl, successUrl);
//
//            for (Links link : payment.getLinks()) {
//                if (link.getRel().equals("approval_url")) {
//                    return new RedirectView(link.getHref());
//                }
//            }
//
//        } catch (Exception e) {
//            return new RedirectView(cancelUrl);
//        }
//        return new RedirectView(errorUrl);
//    }
    @PostMapping("/payment/create")
    public ResponseEntity<String> createPayment() {

        try {
            Payment payment = payPalService.createPayment(10.0, "USD", "paypal", "sale", "description", cancelUrl, successUrl);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cancelUrl);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorUrl);
    }
    @GetMapping("/payment/success")
    public ResponseEntity<String> successPayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId)
    {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok("Payment Success");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment Error");
        }
        return ResponseEntity.ok("Payment Success");
    }

    @GetMapping("/payment/cancel")
    public ResponseEntity<String> cancelPayment() {
        return ResponseEntity.ok().body("Payment Cancelled");
    }

    @GetMapping("/payment/error")
    public ResponseEntity<String> errorPayment() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment Error");
    }



}
