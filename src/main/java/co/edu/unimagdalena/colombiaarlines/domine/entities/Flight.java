package co.edu.unimagdalena.colombiaarlines.domine.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import java.util.List;


@Entity
@Table(name= "flights")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String number;
    @Column(nullable = false)
    private OffsetDateTime departureTime;
    @Column(nullable = false)
    private OffsetDateTime arrivalTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_airport_id", nullable = false)
    private Airport origin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airport destination;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "flight_tags",
    joinColumns = @JoinColumn(name = "flight_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    List<SeatInventory> seatInventories = new ArrayList<>();

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getFlights().add(this);
    }

    public void addSeatInventory(SeatInventory seatInventory) {
        seatInventories.add(seatInventory);
        seatInventory.setFlight(this);
    }
}
