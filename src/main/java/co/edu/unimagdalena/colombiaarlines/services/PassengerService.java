package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.PassengerDtos.*;

public interface PassengerService {

    PassengerResponse create(PassengerCreateRequest req);
    
}
