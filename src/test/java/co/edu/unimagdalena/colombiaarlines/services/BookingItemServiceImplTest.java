package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.domine.entities.BookingItem;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Cabin;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.BookingItemRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.BookingRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingItemServiceImplTest {

    @Mock
    private BookingItemRepository repo;

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private FlightRepository flightRepo;

    @InjectMocks
    private BookingItemServiceImpl service;

    // --- CREATE ---
 /*   void shouldAddBookingItem() {
        Long bookingId = 1L;
        Long flightId = 2L;

        Booking booking = Booking.builder()
                .id(bookingId)
                .items(new ArrayList<>())
                .build();

        Flight flight = Flight.builder()
                .id(flightId)
                .build();

        BookingItemCreateRequest req = new BookingItemCreateRequest(Cabin.ECONOMY,new BigDecimal("500.00"),1,bookingId,flightId);

        BookingItem bookingItem = BookingItem.builder()
                .id(10L)
                .booking(booking)
                .flight(flight)
                .cabin(req.cabin())
                .segmentOrder(req.segmentOrder())
                .price(new  BigDecimal("500.00"))
                .build();

        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
        when(flightRepo.findById(flightId)).thenReturn(Optional.of(flight));
        when(repo.save(any(BookingItem.class))).thenReturn(bookingItem);
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

        BookingItemResponse response = service.addItem(bookingId, req);

        // 🧾 Assert
        assertNotNull(response);
        assertEquals(bookingItem.getId(), response.id());
        assertEquals(req.cabin(), response.cabin());
        assertEquals(req.segmentOrder(), response.segmentOrder());

        verify(bookingRepo).findById(bookingId);
        verify(flightRepo).findById(flightId);
        verify(repo).save(any(BookingItem.class));
        verify(bookingRepo).save(booking);
    } */

    // --- UPDATE ---
    @Test
    void shouldUpdateBookingItemSuccessfully() {
        // Arrange
        BookingItemUpdateRequest req = new BookingItemUpdateRequest(Cabin.ECONOMY, new BigDecimal("900.00"),
                1,2L);

        Flight flight = new Flight();
        flight.setId(2L);

        BookingItem existingItem = new BookingItem();
        existingItem.setId(10L);
        existingItem.setFlight(flight);
        existingItem.setPrice(new BigDecimal("700.00"));

        when(repo.findById(req.flightId())).thenReturn(Optional.of(existingItem));
        when(flightRepo.findById(req.flightId())).thenReturn(Optional.of(flight));
        when(repo.save(any(BookingItem.class))).thenReturn(existingItem);

        // Act
        BookingItemResponse result = service.updateBookingItem(req);

        // Assert
        assertThat(result).isNotNull();
        verify(repo).save(existingItem);
    }

    // --- FIND BY BOOKING ID ---
    @Test
    void shouldFindBookingItemsByBookingId() {
        // Arrange
        Long bookingId = 1L;
        BookingItem item1 = new BookingItem();
        item1.setId(1L);
        BookingItem item2 = new BookingItem();
        item2.setId(2L);

        when(repo.findBookingItemByBookingIdOrderBySegmentOrder(bookingId))
                .thenReturn(List.of(item1, item2));

        // Act
        List<BookingItemResponse> result = service.findByBookingIdSegmentOrder(bookingId);

        // Assert
        assertThat(result).hasSize(2);
        verify(repo).findBookingItemByBookingIdOrderBySegmentOrder(bookingId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenBookingItemsEmpty() {
        when(repo.findBookingItemByBookingIdOrderBySegmentOrder(99L))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.findByBookingIdSegmentOrder(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No BookingItems found");
    }

    // --- LIST ---
    @Test
    void shouldListAllBookingItems() {
        when(repo.findAll()).thenReturn(List.of(new BookingItem(), new BookingItem()));
        List<BookingItemResponse> result = service.list();
        assertThat(result).hasSize(2);
        verify(repo).findAll();
    }

    // --- DELETE ---
    @Test
    void shouldDeleteBookingItemSuccessfully() {
        when(repo.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingBookingItem() {
        when(repo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("BookingItem not found");
    }

    // --- SEATS SOLD ---
    @Test
    void shouldGetSeatsSold() {
        Long flightId = 10L;
        Cabin cabin = Cabin.ECONOMY;

        // Mock: existe el vuelo
        when(flightRepo.findById(flightId)).thenReturn(Optional.of(new Flight()));
        // Mock: devuelve el conteo de asientos vendidos
        when(repo.seatsSold(flightId, cabin)).thenReturn(25L);

        Long result = service.seatsSold(flightId, cabin);

        assertEquals(25L, result);
        verify(flightRepo).findById(flightId);
        verify(repo).seatsSold(flightId, cabin);
    }

    // --- TOTAL PRICE ---
    @Test
    void shouldGetTotalPriceSuccessfully() {
        when(repo.getTotalPrice(1L)).thenReturn(new BigDecimal("1200.00"));

        BigDecimal result = service.getTotalPrice(1L);

        verify(repo).getTotalPrice(1L);
        assertEquals(new BigDecimal("1200.00"), result);
    }
}
