package com.hiran.restaurantService.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiran.restaurantService.client.OrderServiceClient;
import com.hiran.restaurantService.dto.RestaurantDTO;
import com.hiran.restaurantService.entity.MenuItem;
import com.hiran.restaurantService.entity.Restaurant;
import com.hiran.restaurantService.service.MenuService;
import com.hiran.restaurantService.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*", allowCredentials = "true")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private MenuService menuService;

    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    @Autowired
    public RestaurantController(RestaurantService restaurantService, ObjectMapper objectMapper) {
        this.restaurantService = restaurantService;
        this.objectMapper = objectMapper;
    }

    @PutMapping("/{id}/availability")
    public Restaurant toggleAvailability(@PathVariable String id) {

        return restaurantService.toggleAvailability(id);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRestaurant(
            @RequestPart("restaurant") String restaurantJson,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {

        try {
            // Add proper logging
            logger.debug("Received restaurant JSON: {}", restaurantJson);

            // Configure ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Parse the JSON to get the restaurant data
            JsonNode rootNode = objectMapper.readTree(restaurantJson);

            // Create DTO and set values
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            restaurantDTO.setName(rootNode.path("name").asText());
            restaurantDTO.setFormattedAddress(rootNode.path("formattedAddress").asText());
            restaurantDTO.setContactNumber(rootNode.path("contactNumber").asText());
            restaurantDTO.setCuisineType(rootNode.path("cuisineType").asText());
            restaurantDTO.setOpeningTime(rootNode.path("openingTime").asText());
            restaurantDTO.setClosingTime(rootNode.path("closingTime").asText());
            restaurantDTO.setEmail(rootNode.path("email").asText());
            restaurantDTO.setRestaurantPassword(rootNode.path("restaurantPassword").asText());

            // Handle location data - two possible approaches:
            // Option 1: If location is sent as a complete GeoJSON object
            if (rootNode.has("location")) {
                JsonNode locationNode = rootNode.path("location");
                if (locationNode.has("coordinates") && locationNode.path("coordinates").isArray()) {
                    restaurantDTO.setLongitude(locationNode.path("coordinates").get(0).asDouble());
                    restaurantDTO.setLatitude(locationNode.path("coordinates").get(1).asDouble());
                }
            }
            // Option 2: If latitude/longitude are sent as separate fields
            else {
                restaurantDTO.setLatitude(rootNode.path("latitude").asDouble());
                restaurantDTO.setLongitude(rootNode.path("longitude").asDouble());
            }

            logger.debug("Parsed restaurant DTO with location: lat={}, long={}",
                    restaurantDTO.getLatitude(), restaurantDTO.getLongitude());

            Restaurant createdRestaurant = restaurantService.createRestaurant(restaurantDTO, coverImage);
            return ResponseEntity.ok(createdRestaurant);

        } catch (Exception e) {
            logger.error("Error creating restaurant", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/view/{id}")
    public Restaurant getById(@PathVariable String id) {
        return restaurantService.getRestaurantById(id);
    }

    @PutMapping("/update/{id}")
    public Restaurant update(@PathVariable String id, @RequestBody Restaurant restaurant) {
        return restaurantService.updateRestaurant(id, restaurant);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
    }

    @PutMapping("/{orderId}/confirm")
    public String confirmOrder(@PathVariable String orderId) {
        return orderServiceClient.updateOrderStatus(
                orderId,
                "RESTAURANT_CONFIRMED"  // Hardcoded status (or pass dynamically)
        );
    }

    @PutMapping("/{orderId}/preparing")
    public String markAsPreparing(@PathVariable String orderId) {
        return orderServiceClient.updateOrderStatus(
                orderId,
                "PREPARING"
        );
    }

    @PutMapping("/{orderId}/ready")
    public String markAsReady(@PathVariable String orderId) {
        return orderServiceClient.updateOrderStatus(
                orderId,
                "READY_FOR_PICKUP"
        );
    }

    @PutMapping("/{orderId}/cancel")
    public String markAsCancel(@PathVariable String orderId) {
        return orderServiceClient.updateOrderStatus(
                orderId,
                "CANCELLED"
        );
    }
}
