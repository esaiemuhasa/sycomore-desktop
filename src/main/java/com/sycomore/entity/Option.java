package com.sycomore.entity;

import javax.persistence.*;

@Entity
@Table(name = "classifiable_option")
public class Option extends Classifiable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Section section;

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
