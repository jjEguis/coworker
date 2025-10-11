package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.PassengerUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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