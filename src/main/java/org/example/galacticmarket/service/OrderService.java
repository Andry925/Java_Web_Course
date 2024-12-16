package org.example.galacticmarket.service;

import org.example.galacticmarket.dto.OrderDTO;
import org.example.galacticmarket.mappers.OrderMapper;
import org.example.galacticmarket.repository.OrderRepository;
import org.example.galacticmarket.repository.ProductRepository;
import org.example.galacticmarket.repository.entity.OrderEntity;
import org.example.galacticmarket.repository.entity.ProductEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDTO> getAllOrders() {
        List<OrderEntity> orders = (List<OrderEntity>) orderRepository.findAll();
        return orderMapper.toOrderDTOList(orders);
    }

    public OrderDTO getOrderById(UUID id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
        return orderMapper.toOrderDTO(order);
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        List<ProductEntity> products = orderDTO.getProducts().stream()
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId)))
                .collect(Collectors.toList());

        OrderEntity order = orderMapper.toOrderEntity(orderDTO);
        order.setProducts(products);
        OrderEntity savedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(savedOrder);
    }

    public OrderDTO updateOrder(UUID id, OrderDTO orderDTO) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
        orderEntity.setCustomerName(orderDTO.getCustomerName());
        List<ProductEntity> productEntities = orderMapper.mapIdsToProducts(orderDTO.getProducts());
        orderEntity.setProducts(productEntities);
        orderEntity = orderRepository.save(orderEntity);
        return orderMapper.toOrderDTO(orderEntity);
    }

    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}