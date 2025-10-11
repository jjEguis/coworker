package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.api.DTOs.PassengerDtos.PassengerCreateRequest;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.api.DTOs.PassengerDtos.PassengerUpdateRequest;

import java.util.List;


public interface PassengerService {

    PassengerResponse create(PassengerCreateRequest req);
    PassengerResponse get(Long id);
    List<PassengerResponse> list();
    PassengerResponse update(Long id, PassengerUpdateRequest req);
    PassengerResponse getByEmail(String email);
    PassengerResponse getByEmailAndPassengerProfile(String email);
    void delete(Long id);
}