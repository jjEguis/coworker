package co.edu.unimagdalena.colombiaarlines.services.mapper;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Passenger;
import co.edu.unimagdalena.colombiaarlines.domine.entities.PassengerProfile;

public class PassengerMapper {

    public static Passenger toEntity(PassengerDtos.PassengerCreateRequest req) {
        return Passenger.builder()
                .fullName(req.fullName())
                .email(req.email())
                .passengerProfile(toProfileEntity(req.profile()))
                .build();
    }

    public static void updateEntity(Passenger passenger, PassengerDtos.PassengerUpdateRequest req) {
        passenger.setFullName(req.fullName());
        passenger.setEmail(req.email());
        passenger.setPassengerProfile(toProfileEntity(req.profile()));
    }

    // Método Helper para actualizar la entidad anidada PassengerProfil
    public static void updateProfileEntity(PassengerProfile profile, PassengerDtos.PassengerProfileDto reqProfile) {
        if (reqProfile == null) return;

        // Solo actualiza los campos si son proporcionados
        if (reqProfile.phone() != null) {
            profile.setPhone(reqProfile.phone());
        }
        if (reqProfile.countryCode() != null) {
            profile.setCountryCode(reqProfile.countryCode());
        }
    }


    public static PassengerDtos.PassengerResponse toResponse(Passenger passenger) {
        return new PassengerDtos.PassengerResponse(
                passenger.getId(),
                passenger.getFullName(),
                passenger.getEmail(),
                toProfileDto(passenger.getPassengerProfile())
        );
    }

    // Helpers para mapear el perfil
    private static PassengerProfile toProfileEntity(PassengerDtos.PassengerProfileDto dto) {
        if (dto == null) return null;
        return PassengerProfile.builder()
                .phone(dto.phone())
                .countryCode(dto.countryCode())
                .build();
    }

    private static PassengerDtos.PassengerProfileDto toProfileDto(PassengerProfile profile) {
        if (profile == null) return null;
        return new PassengerDtos.PassengerProfileDto(
                profile.getPhone(),
                profile.getCountryCode()
        );
    }
}
