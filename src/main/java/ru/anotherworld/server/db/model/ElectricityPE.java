package ru.anotherworld.server.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ELECTRICITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricityPE implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public ElectricityPE(Long day, Long night, Float debt, Boolean active, ApartmentPE apartment) {
        this.day = day;
        this.night = night;
        this.debt = debt != null ? debt : 0.0f;
        this.active = active != null ? active : true;
        this.apartment = apartment;
        this.id = apartment.getId();
    }

    @Id
    @Column(name = "APARTMENT_ID")
    private Integer id;

    @Column(name = "DAY_CONSUMPTION", nullable = false)
    private Long day;

    @Column(name = "NIGHT_CONSUMPTION", nullable = false)
    private Long night;

    @Column(name = "DEBT", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private Float debt = 0.0f;

    @Column(name = "ACTIVE", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APARTMENT_ID", nullable = false)
    @JsonIgnore
    private ApartmentPE apartment;
}
