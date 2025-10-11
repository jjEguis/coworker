package co.edu.unimagdalena.colombiaarlines.domine.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seatInventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

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
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    // Mas metodos Helper
    public void setFlight(Flight flight) {
        // Ya  existe la relacion
        if(this.flight != null){
            this.flight.getSeatInventories().remove(this);
        }
        this.flight = flight;
        //Agregamos la nueva relacion
        if (flight != null
            // Is Unnecessary? //   && !flight.getSeatInventories().contains(this)
            ) {
            this.flight.getSeatInventories().add(this);
        }
    }
}
