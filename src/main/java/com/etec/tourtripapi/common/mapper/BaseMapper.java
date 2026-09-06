package com.etec.tourtripapi.common.mapper;

import java.util.Collections;
import java.util.List;

public interface BaseMapper<E, REQ, RES> {

    E toEntity(REQ request);

    RES toResponse(E entity);

    void updateEntity(E entity, REQ request);

    default List<RES> toResponseList(List<E> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }
}
