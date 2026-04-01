package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
public abstract class BaseEntity {
    //Es el registro de cuando fue creado el usuario
    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;
    //Es el registro de cuando fue actualizado el usuario
    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @PrePersist
    public void prePersist() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updated = LocalDateTime.now();
    }
}

