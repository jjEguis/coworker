package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.domine.entities.Booking;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.BookingRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.PassengerRepository;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingDtos.*;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.BookingItemDtos.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;


    @Test
    void testCreateBooking() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(Passenger.builder().id(1L).fullName("Ana")
                .email("a@d.com").build()));
        when(bookingRepository.save(any())).thenAnswer(inv -> { Booking b = inv.getArgument(0); b.setId(9L); return b; });

        var bookingResponse = bookingService.create(new BookingCreateRequest(1L,
                OffsetDateTime.now(),new ArrayList<>()));
        assertThat(bookingResponse.id()).isEqualTo(9L);
        assertThat(bookingResponse.passenger().email()).isEqualTo("a@d.com");
    }

    @Test
    void testFindByPassengerEmail() {
        String email = "test@mail.com";
        Pageable pageable = PageRequest.of(0, 5);

        Passenger passenger = Passenger.builder()
                .id(1L)
                .email(email)
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .createdAt(OffsetDateTime.now())
                .passenger(passenger)
                .items(new ArrayList<>())
                .build();

        Page<Booking> bookingPage = new PageImpl<>(List.of(booking));

        when(bookingRepository.findBookingByPassenger_EmailOrderByCreatedAtDesc(email, pageable))
                .thenReturn(bookingPage);

        Page<BookingResponse> result = bookingService.findByPassenger_Email(email, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(email, result.getContent().getFirst().passenger().email());

        verify(bookingRepository, times(1))
                .findBookingByPassenger_EmailOrderByCreatedAtDesc(email, pageable);

    }

    @Test
    void testUpdateBooking() {
        Booking existingBooking = Booking.builder()
                .id(1L)
                .items(new ArrayList<>())
                .build();

        Flight flight = new Flight();
        flight.setId(1L);

        BookingItemCreateRequest itemReq = new BookingItemCreateRequest(Cabin.ECONOMY,new BigDecimal("200.53"),
                1, existingBooking.getId(), flight.getId());
        BookingUpdateRequest req = new BookingUpdateRequest(List.of(itemReq));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existingBooking));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(bookingRepository.save(any(Booking.class))).thenReturn(existingBooking);

        BookingResponse response = bookingService.update(1L, req);

        assertNotNull(response);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testSearchBooking() {
        Booking booking = Booking.builder()
                .id(1L)
                .createdAt(OffsetDateTime.now())
                .build();

        when(bookingRepository.searchBooking(1L)).thenReturn(booking);

        BookingResponse response = bookingService.searchBooking(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
        verify(bookingRepository, times(1)).searchBooking(1L);
    }

    @Test
    void testDeleteBooking() {
        Booking booking = new Booking();
        booking.setId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.delete(1L);

        verify(bookingRepository, times(1)).delete(booking);
    }
}
