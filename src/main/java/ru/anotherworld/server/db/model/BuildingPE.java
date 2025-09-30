/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
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
@Table(name = "BUILDING")
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
    @Column(name = "BUILDING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "BUILDING_NAME", nullable = false)
    private String name;

    @Column(name = "BUILDING_CODE", nullable = false, unique = true)
    private String code;

    @OneToMany(mappedBy = "building", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ApartmentPE> apartments = new ArrayList<>();
}
