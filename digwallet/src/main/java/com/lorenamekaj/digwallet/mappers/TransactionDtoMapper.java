package com.lorenamekaj.digwallet.mappers;

import com.lorenamekaj.digwallet.dtos.TransactionDto;
import com.lorenamekaj.digwallet.dtos.UserDto;
import com.lorenamekaj.digwallet.transactions.Transactions;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TransactionDtoMapper implements Function<Transactions, TransactionDto> {

    private UserDtoMapper userDtoMapper = new UserDtoMapper();

    @Override
    public TransactionDto apply(Transactions transactions) {
        return new TransactionDto(
                transactions.getId(),
                userDtoMapper.apply(transactions.getEarner()),
                userDtoMapper.apply(transactions.getPayer()),
                transactions.getAmount()
        );
    }
}
