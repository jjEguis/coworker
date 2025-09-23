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
    @Column(nullable = false, length = 120)
    private String code;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, length = 120)
    private String city;
    @OneToMany(mappedBy = "airport")
    @Builder.Default
    private List<Flight> flightsOrigin = new ArrayList<Flight>();
    @OneToMany(mappedBy = "airport")
    @Builder.Default
    private List<Flight> flightsDestination = new ArrayList<Flight>();

    public void addFlightOrigin(Flight flight) {
        flightsOrigin.add(flight);
        flight.setOrigin(this);
    }

    public void addFlightDestination(Flight flight) {
        flightsDestination.add(flight);
        flight.setDestination(this);
    }

}
