package org.example.galacticmarket.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.galacticmarket.domain.Order;
import org.example.galacticmarket.domain.Product;
import org.example.galacticmarket.dto.OrderDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-08T14:54:55+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDTO toOrderDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO.OrderDTOBuilder orderDTO = OrderDTO.builder();

        orderDTO.id( order.getId() );
        List<Product> list = order.getProducts();
        if ( list != null ) {
            orderDTO.products( new ArrayList<Product>( list ) );
        }
        orderDTO.customerName( order.getCustomerName() );
        orderDTO.createdAt( order.getCreatedAt() );
        orderDTO.updatedAt( order.getUpdatedAt() );

        return orderDTO.build();
    }

    @Override
    public Order toOrder(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.id( orderDTO.getId() );
        List<Product> list = orderDTO.getProducts();
        if ( list != null ) {
            order.products( new ArrayList<Product>( list ) );
        }
        order.customerName( orderDTO.getCustomerName() );
        order.createdAt( orderDTO.getCreatedAt() );
        order.updatedAt( orderDTO.getUpdatedAt() );

        return order.build();
    }

    @Override
    public List<OrderDTO> toOrderDTOList(List<Order> ordersList) {
        if ( ordersList == null ) {
            return null;
        }

        List<OrderDTO> list = new ArrayList<OrderDTO>( ordersList.size() );
        for ( Order order : ordersList ) {
            list.add( toOrderDTO( order ) );
        }

        return list;
    }

    @Override
    public List<Order> toOrderEntityList(List<OrderDTO> ordersDTOList) {
        if ( ordersDTOList == null ) {
            return null;
        }

        List<Order> list = new ArrayList<Order>( ordersDTOList.size() );
        for ( OrderDTO orderDTO : ordersDTOList ) {
            list.add( toOrder( orderDTO ) );
        }

        return list;
    }
}
