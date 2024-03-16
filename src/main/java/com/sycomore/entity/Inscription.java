package com.sycomore.entity;

import javax.persistence.*;

@Entity
@Table(name = "inscription")
public class Inscription extends PersistableEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Promotion promotion;

    @Column(name = "total_paid_cash", insertable = false, precision = 16, scale = 2)
    private Double totalPaidCash;//total argent comptant déjà payé par l'élève

    @Column(name = "total_related_fees", insertable = false, precision = 16, scale = 2)
    private Double totalRelatedFees;//Total frais connexe que doit payer l'élève

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Double getTotalPaidCash() {
        return totalPaidCash;
    }

    public void setTotalPaidCash(Double totalPaidCash) {
        this.totalPaidCash = totalPaidCash;
    }

    public Double getTotalRelatedFees() {
        return totalRelatedFees;
    }

    public void setTotalRelatedFees(Double totalRelatedFees) {
        this.totalRelatedFees = totalRelatedFees;
    }
}
