package co.edu.unimagdalena.colombiaarlines.domine.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "passegerProfiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class PassengerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String countryCode;
    @OneToOne(optional = false)
    @JoinColumn(name = "passenger_id",unique = true)
    private Passenger passenger;
}
