package com.microservice.order_service.service;

import com.microservice.order_service.client.InventoryClient;
import com.microservice.order_service.dto.OrderRequest;
import com.microservice.order_service.model.Order;
import com.microservice.order_service.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final InventoryClient inventoryClient;

  public void placeOrder(OrderRequest orderRequest) throws RuntimeException {
    var isInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
    if (isInStock) {
      Order order =
          Order.builder()
              .orderNumber(UUID.randomUUID().toString())
              .price(orderRequest.price())
              .skuCode(orderRequest.skuCode())
              .quantity(orderRequest.quantity())
              .build();
      orderRepository.save(order);
    } else {
      throw new RuntimeException(
          "Product with SkuCode " + orderRequest.skuCode() + " is not in stock");
    }
  }
}
