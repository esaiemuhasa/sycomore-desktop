package com.sycomore.entity;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass()
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PersistableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    protected Integer id;

    @Column(name = "recording_date", nullable = false, updatable = false)
    protected Date recordingDate;

    @Column(name = "updating_date", insertable = false)
    protected Date updatingDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRecordingDate() {
        return recordingDate;
    }

    public void setRecordingDate(Date recordingDate) {
        this.recordingDate = recordingDate;
    }

    public Date getUpdatingDate() {
        return updatingDate;
    }

    public void setUpdatingDate(Date updatingDate) {
        this.updatingDate = updatingDate;
    }
}
