package com.lorenamekaj.digwallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PaymentRequestException extends RuntimeException {
    public PaymentRequestException(String message) {
        super(message);
    }
}
