package org.example.galacticmarket.mappers;


import org.example.galacticmarket.domain.Order;
import org.example.galacticmarket.domain.Product;
import org.example.galacticmarket.dto.OrderDTO;
import org.example.galacticmarket.repository.entity.OrderEntity;
import org.example.galacticmarket.repository.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderMapper {

    @Mapping(source = "products", target = "products", qualifiedByName = "mapIdsToProducts")
    OrderEntity toOrderEntity(OrderDTO orderDTO);

    @Mapping(source = "products", target = "products", qualifiedByName = "mapProductsToIds")
    OrderDTO toOrderDTO(OrderEntity orderEntity);

    List<OrderDTO> toOrderDTOList(List<OrderEntity> orders);

    @Named("mapIdsToProducts")
    default List<ProductEntity> mapIdsToProducts(List<UUID> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return null;
        }
        return productIds.stream()
                .map(id -> {
                    ProductEntity product = new ProductEntity();
                    product.setId(id); // Set the ID; other fields can be null
                    return product;
                })
                .collect(Collectors.toList());
    }

    @Named("mapProductsToIds")
    default List<UUID> mapProductsToIds(List<ProductEntity> products) {
        if (products == null || products.isEmpty()) {
            return null;
        }
        return products.stream()
                .map(ProductEntity::getId)
                .collect(Collectors.toList());
    }
}
