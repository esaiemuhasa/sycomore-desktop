package com.sycomore.entity;

import javax.persistence.*;

@Entity
@Table( name = "study_fees_config" )
public class StudyFeesConfig extends ConfigurableFees {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false)
    private School school;

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
