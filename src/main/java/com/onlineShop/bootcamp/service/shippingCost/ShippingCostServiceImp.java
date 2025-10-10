package com.onlineShop.bootcamp.service.shippingCost;

import org.springframework.stereotype.Service;

@Service
public class ShippingCostServiceImp implements ShippingCostService{

    @Override
    public double calculateShippingCost(double totalWeight, double totalOrderPrice) {

        if (totalOrderPrice < 100) {
            return 0.00;
        }
        if(totalWeight < 5){
            return 1.50;
        } else if(totalWeight >= 6 && totalWeight <= 10){
            return 10.00;
        } else if(totalWeight > 10){
            return 15.00;
        }

        return 0.00;
    }
}
