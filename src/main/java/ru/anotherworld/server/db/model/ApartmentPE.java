/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
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
@Table(name = "APARTMENT")
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
    @Column(name = "APARTMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "APARTMENT_NUMBER", nullable = false)
    private String number;

    @Column(name = "TOTAL_SQUARE", nullable = false)
    private Float totalSquare;

    @Column(name = "LIVING_SQUARE", nullable = false)
    private Float livingSquare;

    @Column(name = "ROOMS_AMOUNT", nullable = false)
    private Integer roomsAmount;

    @Column(name = "FLOOR", nullable = false)
    private Integer floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUILDING_ID", nullable = false)
    @JsonIgnore
    private BuildingPE building;

    @OneToOne(mappedBy = "apartment", fetch = FetchType.LAZY)
    private ElectricityPE electricity;

    @OneToOne(mappedBy = "apartment", fetch = FetchType.LAZY)
    private WaterSupplyPE waterSupply;
}
