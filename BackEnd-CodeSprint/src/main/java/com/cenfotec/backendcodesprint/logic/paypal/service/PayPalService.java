package com.cenfotec.backendcodesprint.logic.paypal.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
public class PayPalService {

    @Value("${PAYPAL_CLIENT_ID}")
    private String clientId;

    @Value("${PAYPAL_SECRET}")
    private String secret;

    @Value("${PAYPAL_BASE_URL}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        String url = baseUrl + "/v1/oauth2/token";

        String auth = clientId + ":" + secret;
        String encodedAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        String body = "grant_type=client_credentials";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getBody() != null && response.getBody().get("access_token") != null) {
            return response.getBody().get("access_token").toString();
        }

        throw new RuntimeException("No se pudo obtener el access token de PayPal");
    }

    public String createOrder(double amount) {

        String accessToken = getAccessToken();

        String url = baseUrl + "/v2/checkout/orders";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
        {
          "intent": "CAPTURE",
          "application_context": {
            "return_url": "http://localhost:8081/api/v1/paypal/success",
            "cancel_url": "http://localhost:8081/api/v1/paypal/cancel"
          },
          "purchase_units": [
            {
              "amount": {
                "currency_code": "USD",
                "value": "%s"
              }
            }
          ]
        }
        """.formatted(amount);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getBody() != null) {
            return response.getBody().toString();
        }

        throw new RuntimeException("No se pudo crear la orden en PayPal");
    }

    public String captureOrder(String orderId) {
        String accessToken = getAccessToken();

        String url = baseUrl + "/v2/checkout/orders/" + orderId + "/capture";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        HttpEntity<String> request = new HttpEntity<>("{}", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getBody() != null && response.getBody().get("status") != null) {
            return response.getBody().get("status").toString();
        }

        throw new RuntimeException("No se pudo capturar la orden de PayPal");
    }

}