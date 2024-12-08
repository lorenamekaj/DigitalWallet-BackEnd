package com.lorenamekaj.digwallet.dtos;

public record PaymentRequestResponse(
        String nonce, Long paymentRequestId
) {
}
