package com.sycomore.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass()))
            return false;

        try {
            PersistableEntity entity = (PersistableEntity) obj;
            return Objects.equals(entity.id, id);
        } catch (ClassCastException ignored) {}
        return false;
    }
}
