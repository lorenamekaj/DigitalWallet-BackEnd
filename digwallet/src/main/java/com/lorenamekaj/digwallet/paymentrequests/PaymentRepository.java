package com.lorenamekaj.digwallet.paymentrequests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentRequest, Long> {

}
