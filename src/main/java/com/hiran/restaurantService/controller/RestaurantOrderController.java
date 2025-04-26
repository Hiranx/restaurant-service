package com.hiran.restaurantService.controller;

import com.hiran.restaurantService.service.RestaurantOrderService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants/{restaurantId}/orders")
public class RestaurantOrderController {

    private final RestaurantOrderService restaurantOrderService;

    public RestaurantOrderController(RestaurantOrderService restaurantOrderService) {
        this.restaurantOrderService = restaurantOrderService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRestaurantOrders(@PathVariable String restaurantId) {
        return restaurantOrderService.getOrdersForRestaurant(restaurantId);
    }
}