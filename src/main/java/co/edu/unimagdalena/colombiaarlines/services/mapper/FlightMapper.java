package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos;
import co.edu.unimagdalena.colombiaarlines.DTOs.SeatInventoryDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;

import java.util.List;

    public class FlightMapper {

        public static Flight toEntity(FlightDtos.FlightCreateRequest req,
                                      Airline airline,
                                      Airport origin,
                                      Airport destination,
                                      List<Tag> tags) {
            return Flight.builder()
                    .number(req.number())
                    .departureTime(req.departureTime())
                    .arrivalTime(req.arrivalTime())
                    .airline(airline)
                    .origin(origin)
                    .destination(destination)
                    .tags(tags)
                    .build();
        }

        public static void updateEntity(Flight flight,
                                        FlightDtos.FlightUpdateRequest req,
                                        Airline airline,
                                        Airport origin,
                                        Airport destination,
                                        List<Tag> tags) {
            flight.setNumber(req.number());
            flight.setDepartureTime(req.departureTime());
            flight.setArrivalTime(req.arrivalTime());
            flight.setAirline(airline);
            flight.setOrigin(origin);
            flight.setDestination(destination);
            flight.setTags(tags);
        }

        public static FlightDtos.FlightResponse toResponse(Flight f) {
            var seatInventories = f.getSeatInventories() == null ? List.<SeatInventoryDtos.SeatInventoryResponse>of()
                    : f.getSeatInventories().stream()
                    .map(SeatInventoryMapper::toResponse)
                    .toList();


            return new FlightDtos.FlightResponse(
                    f.getId(),
                    f.getNumber(),
                    f.getDepartureTime(),
                    f.getArrivalTime(),
                    AirlineMapper.toResponse(f.getAirline()),
                    AirportMapper.toResponse(f.getOrigin()),
                    AirportMapper.toResponse(f.getDestination()),
                    f.getTags() == null ? List.<Long>of()
                            : f.getTags().stream().map(Tag::getId).toList(),
                    seatInventories);
        }
    }