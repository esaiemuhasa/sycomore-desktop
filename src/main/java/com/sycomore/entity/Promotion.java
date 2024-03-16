package com.sycomore.entity;

import javax.persistence.*;

@Entity
@Table(name = "promotion")
public class Promotion extends PersistableEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    private Option option;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Level level;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, updatable = false)
    private SchoolYear year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    private Category category;

    @Column(name = "total_study_fees", insertable = false, precision = 16, scale = 2)
    private Double totalStudyFees;//total des frais d'études que doivent payer les élèves d'une promotion

    @Column(name = "inscriptions_count", insertable = false)
    private Integer inscriptionsCount;//nombre des élèves inscrit dans une promotion

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public SchoolYear getYear() {
        return year;
    }

    public void setYear(SchoolYear year) {
        this.year = year;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getShortName () {
        String name = getLevel().getShortName();

        if (getCategory() != null) {
            name += " - "+getCategory().getLabel();
        }

        if (getOption() != null) {
            name += " - "+getOption().getShortName();
        }

        return name;
    }

    public String getFullName () {
        String name = getLevel().getFullName();

        if (getCategory() != null) {
            name += " - "+getCategory().getLabel();
        }

        if (getOption() != null) {
            name += " "+getOption().getFullName();
        }

        return name;
    }

    @Override
    public String toString() {
        String name = getLevel().getShortName();

        if (getCategory() != null) {
            name += " - "+getCategory().getLabel();
        }

        if (getOption() != null) {
            name += " "+getOption().getFullName();
        }

        return name;
    }

    public Double getTotalStudyFees() {
        return totalStudyFees;
    }

    public void setTotalStudyFees(Double totalStudyFees) {
        this.totalStudyFees = totalStudyFees;
    }

    public Integer getInscriptionsCount() {
        return inscriptionsCount;
    }

    public void setInscriptionsCount(Integer studentCount) {
        this.inscriptionsCount = studentCount;
    }
}
