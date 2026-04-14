package com.cenfotec.backendcodesprint.api.PaypalController;

import com.cenfotec.backendcodesprint.logic.paypal.service.PayPalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PaypalController {
    private PayPalService paypalService;
    public PaypalController(PayPalService paypalService) {
        this.paypalService = paypalService;
    }

    @GetMapping("/paypal/token")
    public String getToken(){
        return paypalService.getAccessToken();
    }

    @PostMapping("/paypal/create-order")
    public ResponseEntity<String> createOrder(@RequestParam double amount) {
        return ResponseEntity.ok(paypalService.createOrder(amount));
    }

    @PostMapping("/paypal/capture-order")
    public ResponseEntity<String> captureOrder(@RequestParam String orderId) {
        return ResponseEntity.ok(paypalService.captureOrder(orderId));
    }
}
