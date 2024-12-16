package org.example.galacticmarket.service;

import org.example.galacticmarket.dto.OrderDTO;
import org.example.galacticmarket.mappers.OrderMapper;
import org.example.galacticmarket.repository.OrderRepository;
import org.example.galacticmarket.repository.ProductRepository;
import org.example.galacticmarket.repository.entity.OrderEntity;
import org.example.galacticmarket.repository.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private OrderEntity orderEntity;
    private OrderDTO orderDTO;
    private ProductEntity productEntity;

    private UUID orderId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderId = UUID.randomUUID();
        productId = UUID.randomUUID();
        productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setName("Test Product");
        orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        orderEntity.setCustomerName("Test Customer");
        orderEntity.setProducts(Collections.singletonList(productEntity));
        orderDTO = OrderDTO.builder()
                .id(orderId)
                .customerName("Test Customer")
                .products(Collections.singletonList(productId))
                .build();
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(orderEntity));
        when(orderMapper.toOrderDTOList(Collections.singletonList(orderEntity)))
                .thenReturn(Collections.singletonList(orderDTO));

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Customer", result.get(0).getCustomerName());
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(1)).toOrderDTOList(Collections.singletonList(orderEntity));
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.toOrderDTO(orderEntity)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals("Test Customer", result.getCustomerName());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, times(1)).toOrderDTO(orderEntity);
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.getOrderById(orderId);
        });

        assertEquals("Order not found with id: " + orderId, exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void testCreateOrder_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(orderMapper.toOrderEntity(orderDTO)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.toOrderDTO(orderEntity)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        assertEquals("Test Customer", result.getCustomerName());
        verify(productRepository, times(1)).findById(productId);
        verify(orderRepository, times(1)).save(orderEntity);
        verify(orderMapper, times(1)).toOrderEntity(orderDTO);
        verify(orderMapper, times(1)).toOrderDTO(orderEntity);
    }

    @Test
    void testCreateOrder_ProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderDTO);
        });

        assertEquals("Product not found with ID: " + productId, exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void testUpdateOrder_Success() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.mapIdsToProducts(orderDTO.getProducts())).thenReturn(Collections.singletonList(productEntity));
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.toOrderDTO(orderEntity)).thenReturn(orderDTO);

        OrderDTO result = orderService.updateOrder(orderId, orderDTO);

        assertNotNull(result);
        assertEquals("Test Customer", result.getCustomerName());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(orderEntity);
        verify(orderMapper, times(1)).mapIdsToProducts(orderDTO.getProducts());
        verify(orderMapper, times(1)).toOrderDTO(orderEntity);
    }

    @Test
    void testUpdateOrder_NotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrder(orderId, orderDTO);
        });

        assertEquals("Order not found with id: " + orderId, exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void testDeleteOrder_Success() {
        when(orderRepository.existsById(orderId)).thenReturn(true);

        assertDoesNotThrow(() -> orderService.deleteOrder(orderId));

        verify(orderRepository, times(1)).existsById(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void testDeleteOrder_NotFound() {
        when(orderRepository.existsById(orderId)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.deleteOrder(orderId);
        });

        assertEquals("Order not found with id: " + orderId, exception.getMessage());
        verify(orderRepository, times(1)).existsById(orderId);
        verify(orderRepository, never()).deleteById(orderId);
    }
}
