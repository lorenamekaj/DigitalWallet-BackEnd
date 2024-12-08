package com.lorenamekaj.digwallet.transactions;

import com.lorenamekaj.digwallet.dtos.TransactionRegisterRequest;
import com.lorenamekaj.digwallet.exceptions.PaymentRequestException;
import com.lorenamekaj.digwallet.exceptions.ResourceNotFoundException;
import com.lorenamekaj.digwallet.paymentrequests.PaymentRequest;
import com.lorenamekaj.digwallet.paymentrequests.PaymentRequestService;
import com.lorenamekaj.digwallet.profile.ProfileService;
import com.lorenamekaj.digwallet.user.User;
import com.lorenamekaj.digwallet.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final UserService userService;
    private final ProfileService profileService;
    private final PaymentRequestService paymentRequestService;

    public TransactionsService(TransactionsRepository transactionsRepository, UserService userService, ProfileService profileService, PaymentRequestService paymentRequestService) {
        this.transactionsRepository = transactionsRepository;
        this.userService = userService;
        this.profileService = profileService;
        this.paymentRequestService = paymentRequestService;
    }

    public List<Transactions> getAllTransactions() {
        return transactionsRepository.findAll();
    }

    public Transactions getTransactionById(Long id) {
        return transactionsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with ID: " + id + " not found"));
    }

    public List<Transactions> getTransactionsByUserid(Long userId) {
        User user = userService.getUser(userId);
        return transactionsRepository.findByUserId(user.getId());
    }

    @Transactional
    public void makeTransaction(TransactionRegisterRequest request) {
        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequestById(request.paymentRequestId());

        if (!paymentRequestService.isPaymentRequestValid(paymentRequest, request)) {
            throw new PaymentRequestException("Something went wrong!");
        }

        User payer = paymentRequest.getProfile().getUser();
        User earner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (payer.getId().equals(earner.getId())){
            throw new PaymentRequestException("You can't pay yourself");
        }

        profileService.creditProfile(payer.getId(), paymentRequest.getAmount());
        profileService.debitProfile(earner.getId(), paymentRequest.getAmount());

        System.out.println(payer);
        System.out.println(earner);

        Transactions transaction = new Transactions(
            payer, earner, paymentRequest.getAmount()
        );
        paymentRequestService.exercisePaymentRequest(paymentRequest);
        transactionsRepository.save(transaction);
    }
}
