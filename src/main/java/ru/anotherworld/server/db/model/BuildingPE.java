package ru.anotherworld.server.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "building")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingPE implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public BuildingPE(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Id
    @Column(name = "building_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "building_name", nullable = false)
    private String name;

    @Column(name = "building_code", nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ApartmentPE> apartments = new ArrayList<>();
}
