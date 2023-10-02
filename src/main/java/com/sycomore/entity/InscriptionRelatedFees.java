package com.sycomore.entity;

import javax.persistence.*;

@Entity
@Table(name = "inscription_related_fees")
public class InscriptionRelatedFees extends PersistableEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Inscription inscription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private RelatedFeesConfig config;

    public Inscription getInscription() {
        return inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public RelatedFeesConfig getConfig() {
        return config;
    }

    public void setConfig(RelatedFeesConfig config) {
        this.config = config;
    }
}
