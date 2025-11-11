package ru.anotherworld.server.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apartment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentPE implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public ApartmentPE(String number, Float totalSquare, Float livingSquare, Integer roomsAmount, Integer floor, BuildingPE building) {
        this.number = number;
        this.totalSquare = totalSquare;
        this.livingSquare = livingSquare;
        this.roomsAmount = roomsAmount;
        this.floor = floor;
        this.building = building;
    }

    @Id
    @Column(name = "apartment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "apartment_number", nullable = false)
    private String number;

    @Column(name = "total_square", nullable = false)
    private Float totalSquare;

    @Column(name = "living_square", nullable = false)
    private Float livingSquare;

    @Column(name = "rooms_amount", nullable = false)
    private Integer roomsAmount;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    @JsonIgnore
    private BuildingPE building;

    @OneToOne(mappedBy = "apartment", fetch = FetchType.LAZY)
    private ElectricityPE electricity;

    @OneToOne(mappedBy = "apartment", fetch = FetchType.LAZY)
    private WaterSupplyPE waterSupply;
}
