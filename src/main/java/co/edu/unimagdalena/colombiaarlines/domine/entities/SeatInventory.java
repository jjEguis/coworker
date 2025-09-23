package co.edu.unimagdalena.colombiaarlines.domine.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seatInventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SeatInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Cabin cabin;
    @Column(nullable = false)
    private Integer totalSeats;
    @Column(nullable = false)
    private Integer availableSeats;

    // 🔗 Relación con Flight
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
}
