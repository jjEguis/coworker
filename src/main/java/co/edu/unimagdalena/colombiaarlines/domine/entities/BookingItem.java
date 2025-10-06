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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Cabin cabin;

    @JoinColumn(name = "price", nullable = false)
    private BigDecimal price;

    @JoinColumn(name = "segmentOrder",  nullable = false)
    private Integer segmentOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
}