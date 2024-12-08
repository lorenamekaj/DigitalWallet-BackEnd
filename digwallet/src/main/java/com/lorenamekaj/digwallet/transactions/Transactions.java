package com.lorenamekaj.digwallet.transactions;

import com.lorenamekaj.digwallet.user.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "earner_id", referencedColumnName = "id")
    private User earner;

    @ManyToOne
    @JoinColumn(name = "payer_id", referencedColumnName = "id")
    private User payer;

    @Column(nullable = false)
    private Double amount;


    public Transactions() {}

    public Transactions(User earner, User payer, Double amount) {
        this.earner = earner;
        this.payer = payer;
        this.amount = amount;
    }

    public Transactions(Long id, User earner, User payer, Double amount) {
        this.id = id;
        this.earner = earner;
        this.payer = payer;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEarner() {
        return earner;
    }

    public void setEarner(User earner) {
        this.earner = earner;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transactions that = (Transactions) o;
        return Objects.equals(id, that.id) && Objects.equals(earner, that.earner) && Objects.equals(payer, that.payer) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, earner, payer, amount);
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", earner=" + earner +
                ", payer=" + payer +
                ", amount=" + amount +
                '}';
    }
}
