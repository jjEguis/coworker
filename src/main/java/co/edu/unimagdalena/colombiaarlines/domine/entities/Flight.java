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
    @Column
    private String number;
    @Column
    private OffsetDateTime departureTime;
    @Column
    private OffsetDateTime arrivalTime;
    @ManyToOne(optional = true)
    @JoinColumn(name = "airline_id")
    private Airline airline;
    @ManyToOne(optional = false)
    @JoinColumn(name = "origin_airport_id")
    private Airport origin;
    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_airport_id")
    private Airport destination;
    @ManyToMany
    @JoinTable(name = "flight_tags",
    joinColumns = @JoinColumn(name = "flight_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();
    @OneToMany(mappedBy = "flight")
    @Builder.Default
    private List<SeatInventory> seatInventories = new ArrayList<>();

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getFlights().add(this);
    }

    public void addSeatInventory(SeatInventory seatInventory) {
        seatInventories.add(seatInventory);
        seatInventory.setFlight(this);
    }
}
