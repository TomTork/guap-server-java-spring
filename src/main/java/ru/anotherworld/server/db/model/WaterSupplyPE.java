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
@Table(name = "WATER_SUPPLY")
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
    @Column(name = "APARTMENT_ID")
    private Integer id;

    @Column(name = "COLD_CONSUMPTION", nullable = false)
    private Long cold;

    @Column(name = "HOT_CONSUMPTION", nullable = false)
    private Long hot;

    @Column(name = "DEBT", nullable = false, columnDefinition = "FLOAT DEFAULT 0")
    private Float debt = 0.0f;

    @Column(name = "ACTIVE", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APARTMENT_ID", nullable = false)
    @JsonIgnore
    private ApartmentPE apartment;
}
