package com.onlineShop.bootcamp.service.shippingCost;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShippingCostServiceImpTest {
// write test
    private final ShippingCostServiceImp shippingCostService = new ShippingCostServiceImp();

    @Test
    void calculateShippingCostReturnsFreeShippingForOrdersBelowThreshold() {
        double shippingCost = shippingCostService.calculateShippingCost(3.0, 80.0);

        assertThat(shippingCost).isEqualTo(0.0);
    }

    @Test
    void calculateShippingCostUsesWeightBracketsWhenThresholdMet() {
        assertThat(shippingCostService.calculateShippingCost(4.0, 120.0)).isEqualTo(1.50);
        assertThat(shippingCostService.calculateShippingCost(8.0, 120.0)).isEqualTo(10.00);
        assertThat(shippingCostService.calculateShippingCost(12.0, 120.0)).isEqualTo(15.00);
    }
}
