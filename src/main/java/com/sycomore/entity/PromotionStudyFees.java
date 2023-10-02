package com.sycomore.entity;

import javax.persistence.*;

@Entity
@Table(name = "promotion_study_fees")
public class PromotionStudyFees extends PersistableEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private StudyFeesConfig config;

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public StudyFeesConfig getConfig() {
        return config;
    }

    public void setConfig(StudyFeesConfig config) {
        this.config = config;
    }
}
