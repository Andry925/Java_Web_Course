package org.example.galacticmarket.mappers;


import org.example.galacticmarket.domain.Order;
import org.example.galacticmarket.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO toOrderDTO(Order order);

    Order toOrder(OrderDTO orderDTO);

    List<OrderDTO> toOrderDTOList(List<Order> ordersList);

    List<Order> toOrderEntityList(List<OrderDTO> ordersDTOList);
}
