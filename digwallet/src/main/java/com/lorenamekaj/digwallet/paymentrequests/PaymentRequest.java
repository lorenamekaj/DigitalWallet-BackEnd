package com.lorenamekaj.digwallet.paymentrequests;

import com.lorenamekaj.digwallet.profile.Profile;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "payment_requests")
public class PaymentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String nonce;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean realized;

    public PaymentRequest() {}

    public PaymentRequest(Profile profile, Double amount, String nonce, LocalDateTime createdAt, Boolean realized) {
        this.profile = profile;
        this.amount = amount;
        this.nonce = nonce;
        this.createdAt = createdAt;
        this.realized = realized;
    }

    public PaymentRequest(Long id, Profile profile, Double amount, String nonce, LocalDateTime createdAt, Boolean realized) {
        this.id = id;
        this.profile = profile;
        this.amount = amount;
        this.nonce = nonce;
        this.createdAt = createdAt;
        this.realized = realized;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRealized() {
        return realized;
    }

    public void setRealized(Boolean realized) {
        this.realized = realized;
    }
}
