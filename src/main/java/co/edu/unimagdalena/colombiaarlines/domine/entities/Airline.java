package co.edu.unimagdalena.colombiaarlines.domine.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name= "airlines")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String code;
    @Column //(nullable = false, length = 120)
    private String name;
    @OneToMany(mappedBy = "airline")
    @Builder.Default
    private Set<Flight> flights = new HashSet<Flight>();

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.setAirline(this);
    }




}
