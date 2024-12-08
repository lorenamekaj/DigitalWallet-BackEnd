package com.lorenamekaj.digwallet.dtos;

public record TransactionRegisterRequest(
        String nonce,
        Long paymentRequestId
) {
}
