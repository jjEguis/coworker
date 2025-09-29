package co.edu.unimagdalena.colombiaarlines.domine.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private OffsetDateTime createdAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;
    @OneToMany(mappedBy = "booking")
    @Builder.Default
    private List<BookingItem> items = new ArrayList<>();

    public void addItem(BookingItem bookingItem){

        items.add(bookingItem);
        bookingItem.setBooking(this);

    }

}
