package com.sycomore.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "school_year")
public class SchoolYear extends PersistableEntity {

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private boolean archived;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
