package com.lorenamekaj.digwallet.paymentrequests;

import com.lorenamekaj.digwallet.dtos.PaymentRequestRequest;
import com.lorenamekaj.digwallet.dtos.PaymentRequestResponse;
import com.lorenamekaj.digwallet.dtos.TransactionRegisterRequest;
import com.lorenamekaj.digwallet.exceptions.PaymentRequestException;
import com.lorenamekaj.digwallet.exceptions.ResourceNotFoundException;
import com.lorenamekaj.digwallet.profile.Profile;
import com.lorenamekaj.digwallet.profile.ProfileService;
import com.lorenamekaj.digwallet.user.User;
import com.lorenamekaj.digwallet.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentRequestService {

    private final UserService userService;
    private final ProfileService profileService;
    private final PaymentRepository paymentRepository;

    public PaymentRequestService(UserService userService, ProfileService profileService, PaymentRepository paymentRepository) {
        this.userService = userService;
        this.profileService = profileService;
        this.paymentRepository = paymentRepository;
    }

    public List<PaymentRequest> getAllPaymentRequests() {
        return paymentRepository.findAll();
    }

    public PaymentRequest getPaymentRequestById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment Request with ID: " + id + " not found"));
    }

    public PaymentRequestResponse addPaymentRequest(PaymentRequestRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Profile profile = profileService.getProfileByUserId(user.getId());

        if (profile.getBalance() < request.amount()) {
            throw new PaymentRequestException("Not enough balance for this request!");
        }

        String nonce = UUID.randomUUID().toString();
        PaymentRequest paymentRequest = new PaymentRequest(
                profile, request.amount(), nonce, LocalDateTime.now(), false
        );

        PaymentRequest payment = paymentRepository.save(paymentRequest);
        return new PaymentRequestResponse(nonce, payment.getId());
    }

    public boolean isPaymentRequestValid(PaymentRequest paymentRequest, TransactionRegisterRequest transactionRegisterRequest) {
        if (paymentRequest.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(15))) {
            throw new PaymentRequestException("Payment Request has expired!");
        }
        else if (!transactionRegisterRequest.nonce().equals(paymentRequest.getNonce())) {
            throw new PaymentRequestException("Nonce is wrong!");
        }
        else if (paymentRequest.getRealized()) {
            throw new PaymentRequestException("This request has already been exercised!");
        }
        else if (paymentRequest.getAmount() > paymentRequest.getProfile().getBalance()){
            throw new PaymentRequestException("Not enough balance to complete this action!");
        }
        else {
            return true;
        }
    }

    public void exercisePaymentRequest(PaymentRequest request) {
        PaymentRequest paymentRequest = getPaymentRequestById(request.getId());
        paymentRequest.setRealized(true);
        paymentRepository.save(paymentRequest);
    }

}
