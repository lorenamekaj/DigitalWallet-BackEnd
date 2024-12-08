package com.lorenamekaj.digwallet.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    @Query("SELECT t FROM Transactions t WHERE t.earner.id = :userId")
    List<Transactions> findByUserId(@Param("userId") Long userId);
}
