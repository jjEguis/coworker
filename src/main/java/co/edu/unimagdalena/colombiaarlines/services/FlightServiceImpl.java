package co.edu.unimagdalena.colombiaarlines.services;

import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.FlightCreateRequest;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.FlightResponse;
import co.edu.unimagdalena.colombiaarlines.DTOs.FlightDtos.FlightUpdateRequest;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airline;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Airport;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Flight;
import co.edu.unimagdalena.colombiaarlines.domine.entities.Tag;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirlineRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.AirportRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.FlightRepository;
import co.edu.unimagdalena.colombiaarlines.domine.repositories.TagRepository;
import co.edu.unimagdalena.colombiaarlines.exception.NotFoundException;
import co.edu.unimagdalena.colombiaarlines.services.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepo;
    // Repositorios de entidades relacionadas
    private final AirlineRepository airlineRepo;
    private final AirportRepository airportRepo;
    private final TagRepository tagRepo;
    
    // Inyección de la interfaz MapStruct
    private final FlightMapper flightMapper; 


    @Override
    public FlightResponse create(FlightCreateRequest req) {
        Airline airline = airlineRepo.findById(req.airlineId())
            .orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(req.airlineId())));

        Airport origin = airportRepo.findById(req.originAirportId())
            .orElseThrow(() -> new NotFoundException("Origin Airport %d not found".formatted(req.originAirportId())));

        Airport destination = airportRepo.findById(req.destinationAirportId())
            .orElseThrow(() -> new NotFoundException("Destination Airport %d not found".formatted(req.destinationAirportId())));

        List<Long> tagIds = req.tagsId();
        // Buscar todas las etiquetas solicitadas
        List<Tag> tags = tagIds == null || tagIds.isEmpty() ? List.of() : tagRepo.findAllById(tagIds);
        
        if (tags.size() != tagIds.size()) {
             throw new NotFoundException("One or more Tags were not found.");
        }

        // 2. Mapeo: El Mapper recibe las entidades ya cargadas
        Flight flight = flightMapper.toEntity(req, airline, origin, destination, tags);

        // 3. Persistir y Mapear a Respuesta
        return flightMapper.toResponse(flightRepo.save(flight));
    }


    @Override @Transactional(readOnly = true)
    public FlightResponse get(Long id) {
        return flightRepo.findById(id)
                .map(flightMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public List<FlightResponse> list() {
        return flightRepo.findAll().stream()
                .map(flightMapper::toResponse)
                .toList();
    }
    
    @Override
    public void delete(Long id) {
        if (!flightRepo.existsById(id)) {
            throw new NotFoundException("Flight %d not found".formatted(id));
        }
        flightRepo.deleteById(id);
    }


    @Override
    public FlightResponse update(Long id, FlightUpdateRequest req) {

        Flight flight = flightRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(id)));
        
        // Buscar y validar TODAS ahhhh!!!!! las entidades a actualizar
        Long airlineId = req.airlineId() != null ? req.airlineId() : flight.getAirline().getId();
        Airline airline = airlineRepo.findById(airlineId)
            .orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(airlineId)));

        Long originId = req.originAirportId() != null ? req.originAirportId() : flight.getOrigin().getId();
        Airport origin = airportRepo.findById(originId)
            .orElseThrow(() -> new NotFoundException("Origin Airport %d not found".formatted(originId)));

        Long destinationId = req.destinationAirportId() != null ? req.destinationAirportId() : flight.getDestination().getId();
        Airport destination = airportRepo.findById(destinationId)
            .orElseThrow(() -> new NotFoundException("Destination Airport %d not found".formatted(destinationId)));
        
        // Si el request incluye IDs, los reemplazamos. Si no, mantenemos los existentes.
        List<Long> tagIds = req.tagsId();
        List<Tag> tags = flight.getTags(); // Por defecto, mantenemos los tags existentes
        
        if (tagIds != null) {
            tags = tagIds.isEmpty() ? List.of() : tagRepo.findAllById(tagIds);
            if (tags.size() != tagIds.size()) {
                 throw new NotFoundException("One or more Tags were not found for update.");
            }
        }
        
        // actualizar la entidad con MapStruct
        flightMapper.updateEntity(flight, req, airline, origin, destination, tags); 

        // persistir y mapear la Respuesta
        return flightMapper.toResponse(flightRepo.save(flight));
    }

    // Estas ahi Dios?
    @Override
    public FlightResponse addTag(Long flightId, Long tagId) {
        Flight f = flightRepo.findById(flightId).orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flightId)));
        Tag t = tagRepo.findById(tagId).orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tagId)));
        
        // Lógica de negocio: Añadir y asegurar que no haya duplicados (Set lo maneja)
        if (f.getTags().stream().noneMatch(tag -> tag.getId().equals(tagId))) {
            f.getTags().add(t);
        }
        
        return flightMapper.toResponse(f);
    }
    
    @Override
    public FlightResponse removeTag(Long flightId, Long tagId) {
        Flight f = flightRepo.findById(flightId).orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(flightId)));
        Tag t = tagRepo.findById(tagId).orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(tagId)));
        
        // Lógica de negocio: Eliminar la relación
        f.getTags().remove(t);
        
        return flightMapper.toResponse(f);
    }
}