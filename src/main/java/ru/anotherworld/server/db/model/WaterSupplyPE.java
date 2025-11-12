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
@Table(name = "water_supply")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterSupplyPE implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public WaterSupplyPE(Long cold, Long hot, Float debt, Boolean active) {
        this.cold = cold;
        this.hot = hot;
        this.debt = debt != null ? debt : 0.0f;
        this.active = active != null ? active : true;
    }

    @Id
    @Column(name = "apartment_id")
    private Integer id;

    @Column(name = "cold_consumption", nullable = false)
    private Long cold;

    @Column(name = "hot_consumption", nullable = false)
    private Long hot;

    @Column(name = "debt", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private Float debt = 0.0f;

    @Column(name = "active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", nullable = false)
    @JsonIgnore
    private ApartmentPE apartment;
}