package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.model.Space;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractConverter<T, DTO> {

//    T toEntity(DTO dto);
//
//    DTO toDTO(T t);

    public List<Space> fromEntitys(List<SpaceDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return null;
        } else {
            return dtos.stream().map(this::toEntity).collect(Collectors.toList());
        }
    }



    public List<SpaceDTO> toDTOs(List<Space> ts) {
        if (ts == null || ts.isEmpty()) {
            return null;
        } else {
            return ts.stream().map(this::toDTO).collect(Collectors.toList());
        }
    }

    public abstract Space toEntity(SpaceDTO dto);

    public abstract SpaceDTO toDTO(Space space);
}