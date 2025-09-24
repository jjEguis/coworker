package co.edu.unimagdalena.colombiaarlines.domine.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bookingItems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BookingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Cabin cabin;

    private BigDecimal price;

    private Integer segmentOrder;

    // 🔗 Relación con Booking
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // 🔗 Relación con Flight
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
}