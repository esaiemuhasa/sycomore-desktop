package com.sycomore.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "payment")
public class Payment extends PersistableEntity {

    @Column(nullable = false)
    private double amount;

    @Column(name = "day_date", nullable = false)
    private Date dayDate;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Inscription inscription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    private RelatedFeesConfig relatedConfig;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    private StudyFeesConfig studyConfig;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDayDate() {
        return dayDate;
    }

    public void setDayDate(Date dayDate) {
        this.dayDate = dayDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public RelatedFeesConfig getRelatedConfig() {
        return relatedConfig;
    }

    public void setRelatedConfig(RelatedFeesConfig relatedConfig) {
        this.relatedConfig = relatedConfig;
    }

    public StudyFeesConfig getStudyConfig() {
        return studyConfig;
    }

    public void setStudyConfig(StudyFeesConfig studyConfig) {
        this.studyConfig = studyConfig;
    }
}
