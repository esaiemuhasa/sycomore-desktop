package com.sycomore.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entité de categorisation des classes d'études (Ex : A, B, C, D, etc.)
 */
@Entity
@Table(name = "category")
public class Category extends PersistableEntity {

    @Column(nullable = false)
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
