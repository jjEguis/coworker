package co.edu.unimagdalena.colombiaarlines.api.DTOs;

import java.io.Serializable;

public class TagDtos {

    public record TagCreateRequest(String name) implements Serializable {}
    public record TagUpdateRequest(String name) implements Serializable {}
    public record TagResponse(Long id, String name) implements Serializable {}
}
