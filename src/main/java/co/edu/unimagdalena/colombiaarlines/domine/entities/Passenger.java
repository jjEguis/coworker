package co.edu.unimagdalena.colombiaarlines.domine.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "passengers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String email;
    @OneToOne(mappedBy = "passenger")
    private PassengerProfile passengerProfile;

}
