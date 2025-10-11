package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.*;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {PassengerProfileMapper.class})
public interface PassengerMapper {

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "passengerProfile",  ignore = true)
    Passenger toEntity(PassengerCreateRequest req);

    @Mapping(target = "profile", source = "passengerProfile")
    PassengerResponse toResponse(Passenger entity);

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "passengerProfile",  ignore = true)
    void updateEntity(PassengerUpdateRequest req, @MappingTarget Passenger entity);
}
