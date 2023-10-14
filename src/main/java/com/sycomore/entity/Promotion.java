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
    @JoinColumn(nullable = false)
    private SchoolYear year;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    private Category category;

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
        return getShortName();
    }
}
