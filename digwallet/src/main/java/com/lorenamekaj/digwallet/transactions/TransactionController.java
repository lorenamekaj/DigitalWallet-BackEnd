package com.lorenamekaj.digwallet.transactions;

import com.lorenamekaj.digwallet.dtos.TransactionRegisterRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionsService transactionsService;

    public TransactionController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping
    public List<Transactions> getTransactions() {
        return transactionsService.getAllTransactions();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public void makeTransaction(@RequestBody TransactionRegisterRequest request) {
        transactionsService.makeTransaction(request);
    }
}
