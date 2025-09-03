package co.edu.unimagdalena.demojpa.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.Array;
import org.hibernate.mapping.Set;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>() ;

    @OneToOne(mappedBy = "user")
    private DriverLicense license;
}
