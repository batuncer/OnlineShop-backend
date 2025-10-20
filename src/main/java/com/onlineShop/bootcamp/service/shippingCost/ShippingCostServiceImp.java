package com.onlineShop.bootcamp.service.shippingCost;

import org.springframework.stereotype.Service;

@Service
public class ShippingCostServiceImp implements ShippingCostService{

    @Override
    public double calculateShippingCost(double totalPrice, double totalWeight) {


        if(totalWeight < 500 && totalPrice < 100.00){
            return 1.50;
        } else if(totalWeight >= 600 && totalWeight <= 10000 && totalPrice <100.00){
            return 10.00;
        } else if(totalWeight > 10000 && totalPrice < 100.00){
            return 15.00;
        } else if(totalPrice >= 100 ) {
            return 0.00;
        }


        return 0.00;
    }
}
