package com.hiran.restaurantService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service", url = "http://localhost:7002")
public interface OrderNotificationClient {

    @PostMapping("/api/orders/notify-restaurant")
    String notifyRestaurant(@RequestBody String orderDetails);
}
