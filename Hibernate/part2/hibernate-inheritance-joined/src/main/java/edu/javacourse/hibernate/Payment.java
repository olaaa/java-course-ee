package edu.javacourse.hibernate;

import javax.persistence.*;

@Entity
@Table(name = "jc_payment") // таблица существует в бд
@Inheritance(strategy = InheritanceType.JOINED)
// указываем, что будет применяться наследование и указываем его тип
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    protected Integer paymentId;

    @Column(name = "AMOUNT")
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                '}';
    }
}
