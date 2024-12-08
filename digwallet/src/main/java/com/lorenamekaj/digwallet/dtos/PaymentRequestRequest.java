package com.lorenamekaj.digwallet.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequestRequest(
        @NotNull @Positive Double amount
) {

}
