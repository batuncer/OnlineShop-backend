package com.onlineShop.bootcamp.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Generic wrapper for API responses")
public class ApiResponse<T> {
    @Schema(description = "Indicates whether the request was successful", example = "true")
    private boolean success;
    @Schema(description = "Human readable message that describes the response", example = "Operation completed successfully")
    private String message;
    @Schema(description = "Payload of the response. Content varies per endpoint")
    private T data;
}
