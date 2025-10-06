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
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String city;
    @OneToMany(mappedBy = "origin")
    @Builder.Default
    private List<Flight> flightsOrigin = new ArrayList<>();

    @OneToMany(mappedBy = "destination")
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
