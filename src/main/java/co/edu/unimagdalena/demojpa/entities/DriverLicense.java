package co.edu.unimagdalena.demojpa.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
@Entity
@Table(name = "driversLicenses")

public class DriverLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private java.time.LocalDate issuedAt;

    @Column(nullable = false)
    private java.time.LocalDate expiresAt;

    @OneToOne(optional = false)
    private User user;
}
