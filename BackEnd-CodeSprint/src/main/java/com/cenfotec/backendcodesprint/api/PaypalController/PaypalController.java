package com.cenfotec.backendcodesprint.api.PaypalController;

import com.cenfotec.backendcodesprint.logic.SupportProduct.Service.SupportProductService;
import com.cenfotec.backendcodesprint.logic.paypal.service.PayPalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
public class PaypalController {

    private final PayPalService paypalService;
    private final SupportProductService supportProductService;

    public PaypalController(PayPalService paypalService,
                            SupportProductService supportProductService) {
        this.paypalService = paypalService;
        this.supportProductService = supportProductService;
    }

    @GetMapping("/paypal/token")
    public String getToken() {
        return paypalService.getAccessToken();
    }

    @PostMapping("/paypal/create-order")
    public ResponseEntity<String> createOrder(@RequestParam double amount) {
        return ResponseEntity.ok(paypalService.createOrder(amount));
    }

    @PostMapping("/paypal/capture-order")
    public ResponseEntity<String> captureOrder(
            @RequestParam String orderId,
            @RequestParam Long productId) {

        String response = paypalService.captureOrder(orderId);

        if ("COMPLETED".equalsIgnoreCase(response)) {
            supportProductService.markAsSold(productId);
        }

        return ResponseEntity.ok(response);
    }
}