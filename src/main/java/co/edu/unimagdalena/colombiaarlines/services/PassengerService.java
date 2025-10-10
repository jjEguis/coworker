package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerUpdateRequest;

import java.util.List;

public interface PassengerService {

    PassengerResponse create(PassengerCreateRequest req);

    PassengerResponse get(Long id);

    List<PassengerResponse> list();

    PassengerResponse update(Long id, PassengerUpdateRequest req);

    void delete(Long id);
}