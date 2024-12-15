package com.lorenamekaj.digwallet.dtos;

public record TransactionDto(
        Long id,
        UserDto earner,
        UserDto payer,
        Double amount
) {
}
