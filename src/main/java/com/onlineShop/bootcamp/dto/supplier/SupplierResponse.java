package com.onlineShop.bootcamp.dto.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Supplier details returned by supplier endpoints")
public class SupplierResponse {
    @Schema(description = "Supplier identifier", example = "3")
    private Long id;
    @Schema(description = "Supplier company or person name", example = "Leaf & Bean Co.")
    private String name;
}
