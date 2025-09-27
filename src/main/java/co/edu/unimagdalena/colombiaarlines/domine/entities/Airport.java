package co.edu.unimagdalena.colombiaarlines.domine.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "airports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column     // (nullable = false)   // Post-Produccion
    private String code;
    @Column
    private String name;
    @Column  //  (nullable = false)   // Post-produccion
    private String city;
    // Relación para vuelos que tienen este aeropuerto como ORIGEN
    @OneToMany(mappedBy = "origin") // 'origin' es el nombre del campo en Flight
    @Builder.Default
    private List<Flight> flightsOrigin = new ArrayList<>();

    // Relación para vuelos que tienen este aeropuerto como DESTINO
    @OneToMany(mappedBy = "destination") // 'destination' es el nombre del campo en Flight
    @Builder.Default
    private List<Flight> flightsDestination = new ArrayList<>();

    public void addFlightOrigin(Flight flight) {
        flightsOrigin.add(flight);
        flight.setOrigin(this);
    }

    public void addFlightDestination(Flight flight) {
        flightsDestination.add(flight);
        flight.setDestination(this);
    }

}
