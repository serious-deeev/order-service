package org.serious.dev.mapper;

import org.mapstruct.Mapper;
import org.serious.dev.dto.OrderResponseDto;

@Mapper(componentModel = "spring")
public interface OrderResponseMapper {

    OrderResponseDto toOrderResponseDto(Long orderId);
}
