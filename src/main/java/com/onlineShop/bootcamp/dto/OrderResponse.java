package com.onlineShop.bootcamp.dto;
import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import com.onlineShop.bootcamp.entity.Order;
import com.onlineShop.bootcamp.entity.OrderItem;

@Value
@Builder
public class OrderResponse {
    Long orderId;
    OffsetDateTime placedAt;
    String username;
    String shippingAddress;
    List<Item> items;
    BigDecimal subtotal;
    BigDecimal shipping;
    BigDecimal total;

    @Value
    @Builder
    public static class Item {
        Long productId;
        String name;
        int quantity;
        BigDecimal unitPrice;
        BigDecimal lineTotal;
        Integer amountGramsPerUnit;
    }

    public static OrderResponse from(Order o) {
        return OrderResponse.builder()
                .orderId(o.getId())
                .placedAt(o.getPlacedAt())
                .username(o.getUser().getUsername())
                .shippingAddress(o.getShippingAddress())
                .items(o.getItems().stream().map(OrderResponse::toItem).toList())
                .subtotal(o.getSubtotal())
                .shipping(o.getShipping())
                .total(o.getTotal())
                .build();
    }

    private static Item toItem(OrderItem i) {
        return Item.builder()
                .productId(i.getProductId())
                .name(i.getName())
                .quantity(i.getQuantity())
                .unitPrice(i.getUnitPrice())
                .lineTotal(i.getLineTotal())
                .amountGramsPerUnit(i.getAmountGramsPerUnit())
                .build();
    }
}