package com.cohort5.RestBil_System_Backend.service;

import com.cohort5.RestBil_System_Backend.Model.MenuItem;
import com.cohort5.RestBil_System_Backend.Dto.MenuItemRequest;
import com.cohort5.RestBil_System_Backend.Dto.MenuItemResponse;
import com.cohort5.RestBil_System_Backend.Repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for menu item business logic
 */
@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public MenuItemResponse getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        return convertToResponse(menuItem);
    }

    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        
        MenuItem savedItem = menuItemRepository.save(menuItem);
        return convertToResponse(savedItem);
    }

    public MenuItemResponse updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setAvailable(request.getAvailable());
        
        MenuItem updatedItem = menuItemRepository.save(menuItem);
        return convertToResponse(updatedItem);
    }

    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RuntimeException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    public List<MenuItemResponse> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private MenuItemResponse convertToResponse(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getCategory(),
                menuItem.getAvailable()
        );
    }
}
