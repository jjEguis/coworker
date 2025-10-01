package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PassengerMapper {
    @Mapping(source = "profile", target = "passengerProfile")
    Passenger toEntity(PassengerCreateRequest req);
    @Mapping(source = "passengerProfile", target = "profile")
    PassengerResponse toResponse(Passenger entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passegerProfile", ignore = true )
    void updateEntity(PassengerUpdateRequest req, @MappingTarget Passenger entity);


}
