package co.edu.unimagdalena.colombiaarlines.domine.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "passengers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String fullName;
    @Column
    private String email;
    @OneToOne(mappedBy = "passenger")
    private PassengerProfile passengerProfile;

}
