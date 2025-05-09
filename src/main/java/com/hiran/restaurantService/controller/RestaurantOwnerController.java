package com.hiran.restaurantService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiran.restaurantService.dto.MenuItemDTO;
import com.hiran.restaurantService.entity.MenuItem;
import com.hiran.restaurantService.entity.Restaurant;
import com.hiran.restaurantService.service.MenuService;
import com.hiran.restaurantService.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/owner/restaurants")
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*", allowCredentials = "true")
public class RestaurantOwnerController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @PutMapping("/{restaurantId}/availability")
    public Restaurant toggleAvailability(@PathVariable String restaurantId) {
        return restaurantService.toggleAvailability(restaurantId);
    }

    @PutMapping("/{restaurantId}")
    public Restaurant updateRestaurant(@PathVariable String restaurantId,
                                       @RequestBody Restaurant restaurant) {
        return restaurantService.updateRestaurant(restaurantId, restaurant);
    }

    @DeleteMapping("/{restaurantId}")
    public void deleteRestaurant(@PathVariable String restaurantId) {
        restaurantService.deleteRestaurant(restaurantId);
    }

    @PostMapping(value = "/{restaurantId}/menu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItem> addMenuItem(
            @PathVariable String restaurantId,
            @RequestPart("menuItem") String menuItemJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MenuItemDTO menuItemDTO = objectMapper.readValue(menuItemJson, MenuItemDTO.class);

            MenuItem createdItem = menuService.addMenuItem(restaurantId, menuItemDTO, imageFile);
            return ResponseEntity.ok(createdItem);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{restaurantId}/menu/{itemId}")
    public ResponseEntity<MenuItem> updateMenuItem(
            @PathVariable String restaurantId,
            @PathVariable String itemId,
            @RequestBody MenuItemDTO menuItemDTO) {
        return ResponseEntity.ok(menuService.updateMenuItem(restaurantId, itemId, menuItemDTO));
    }

    @DeleteMapping("/{restaurantId}/menu/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable String restaurantId,
            @PathVariable String itemId) {
        menuService.deleteMenuItem(restaurantId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{restaurantId}/menu/{itemId}/availability")
    public ResponseEntity<MenuItem> toggleItemAvailability(
            @PathVariable String restaurantId,
            @PathVariable String itemId) {
        try {
            MenuItem updatedItem = menuService.toggleItemAvailability(restaurantId, itemId);
            return ResponseEntity.ok(updatedItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
