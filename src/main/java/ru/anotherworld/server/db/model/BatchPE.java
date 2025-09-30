package ru.anotherworld.server.db.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "BATCH")
@Data
public class BatchPE implements Serializable {

    public BatchPE() {
    }

    public BatchPE(Integer id, String nomer) {
        this.id = id;
        this.number = nomer;

    }

    @Id
    @Column(name = "BATCH_ID")
    private Integer id;

    @Column(name = "BATCH_NUMBER")
    private String number;

    @Column(name = "BATCH_SCHOOL_ID")
    private Integer school;
}
