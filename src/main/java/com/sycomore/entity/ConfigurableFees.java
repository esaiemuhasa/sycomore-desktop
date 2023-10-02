package com.sycomore.entity;


import javax.persistence.*;
import java.sql.Date;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ConfigurableFees extends PersistableEntity {

    @Column(nullable = false)
    private double amount;

    @Column
    private String caption;

    @Column(name = "start_at")
    private Date start;

    @Column(name = "end_at")
    private Date end;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private SchoolYear year;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public SchoolYear getYear() {
        return year;
    }

    public void setYear(SchoolYear year) {
        this.year = year;
    }
}
