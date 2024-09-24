package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.InventoryRequest;
import com.microservice.inventory_service.dto.InventoryResponse;
import com.microservice.inventory_service.model.Inventory;
import com.microservice.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {
  private final InventoryRepository inventoryRepository;

  @Transactional(readOnly = true)
  public boolean isInStock(String skuCode, Integer quantity) {
    return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
  }

  public InventoryResponse importInventory(InventoryRequest request) {
    var inventory =
        Inventory.builder().skuCode(request.skuCode()).quantity(request.quantity()).build();
    inventory = inventoryRepository.save(inventory);

    return new InventoryResponse(inventory.getSkuCode(), inventory.getQuantity());
  }
}
