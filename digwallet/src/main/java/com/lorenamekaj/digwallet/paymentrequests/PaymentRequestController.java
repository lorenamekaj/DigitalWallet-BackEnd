package com.lorenamekaj.digwallet.paymentrequests;

import com.lorenamekaj.digwallet.dtos.PaymentRequestRequest;
import com.lorenamekaj.digwallet.dtos.PaymentRequestResponse;
import com.lorenamekaj.digwallet.exceptions.UnauthorizedException;
import com.lorenamekaj.digwallet.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-requests")
public class PaymentRequestController {

    private final PaymentRequestService paymentRequestService;

    public PaymentRequestController(PaymentRequestService paymentRequestService) {
        this.paymentRequestService = paymentRequestService;
    }

    @GetMapping
    public List<PaymentRequest> getAllPaymentRequests() {
        return paymentRequestService.getAllPaymentRequests();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public PaymentRequestResponse addPaymentRequest(@RequestBody PaymentRequestRequest request){
        return paymentRequestService.addPaymentRequest(request);
    }
}
