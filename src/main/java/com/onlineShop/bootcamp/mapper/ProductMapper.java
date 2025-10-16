package com.onlineShop.bootcamp.mapper;

import com.onlineShop.bootcamp.dto.product.ProductRequest;
import com.onlineShop.bootcamp.dto.product.ProductResponse;
import com.onlineShop.bootcamp.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest dto) {
        if (dto == null) return null;


        return Product.builder()
                .productName(dto.getTypeName())
                .amountGrams(dto.getAmountGrams())
                .priceGbp(dto.getPriceGbp())
                .brewColor(dto.getBrewColor())
                .caffeineMgPerG(dto.getCaffeineMgPerG())
                .mainMedicinalUse(dto.getMainMedicinalUse())
                .recommendedGramsPerCup(dto.getRecommendedGramsPerCup())
                .description(dto.getDescription())
                .stockQuantity(dto.getStockQuantity())
                .category(dto.getCategory())
                .build();
    }

    public ProductResponse toResponse(Product entity) {
        if (entity == null) return null;

        return ProductResponse.builder()
                .id(entity.getId())
                .supplierName(entity.getSupplier().getName())
                .typeName(entity.getProductName())
                .amountGrams(entity.getAmountGrams())
                .priceGbp(entity.getPriceGbp())
                .brewColor(entity.getBrewColor())
                .caffeineMgPerG(entity.getCaffeineMgPerG())
                .mainMedicinalUse(entity.getMainMedicinalUse())
                .recommendedGramsPerCup(entity.getRecommendedGramsPerCup())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .stockQuantity(entity.getStockQuantity())
                .build();
    }

    public List<ProductResponse> toResponseList(List<Product> entities) {
        if (entities == null || entities.isEmpty()) return List.of();
        return entities.stream().map(this::toResponse).toList();
    }

    public void updateEntity(Product target, ProductRequest src) {
        if (target == null || src == null) return;

        if (src.getTypeName() != null) {
            target.setProductName(src.getTypeName());
        }
        if (src.getAmountGrams() != null) {
            target.setAmountGrams(src.getAmountGrams());
        }
        if (src.getPriceGbp() != null) {
            target.setPriceGbp(src.getPriceGbp());
        }
        if (src.getBrewColor() != null) {
            target.setBrewColor(src.getBrewColor());
        }
        if (src.getCaffeineMgPerG() != null) {
            target.setCaffeineMgPerG(src.getCaffeineMgPerG());
        }
        if (src.getMainMedicinalUse() != null) {
            target.setMainMedicinalUse(src.getMainMedicinalUse());
        }
        if (src.getRecommendedGramsPerCup() != null) {
            target.setRecommendedGramsPerCup(src.getRecommendedGramsPerCup());
        }
    }
}
