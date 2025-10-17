package com.onlineShop.bootcamp.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPreviewResponse {

    private double totalPrice;
    private double shippingCost;
    private List<OrderItemResponse> items;
    
    }