package com.hiran.restaurantService.service;

import com.hiran.restaurantService.client.OrderNotificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationService {

    @Autowired
    private OrderNotificationClient notificationClient;

    // Called when an order is created (HTTP POST)
    public String handleNewOrder(String orderDetails) {
        // 1. Log the order (or save to DB)
        System.out.println("New order received: " + orderDetails);

        // 2. Send confirmation back to Order Service (optional)
        return notificationClient.notifyRestaurant("RESTAURANT_NOTIFIED: " + orderDetails);
    }
}
