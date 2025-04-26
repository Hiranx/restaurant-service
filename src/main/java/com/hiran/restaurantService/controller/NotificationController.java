package com.hiran.restaurantService.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/new-order")
    @SendTo("/topic/orders")
    public String notifyRestaurant(String orderDetails) {
        return "NEW_ORDER: " + orderDetails;
    }
}
