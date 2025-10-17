package com.onlineShop.bootcamp.mapper;

import com.onlineShop.bootcamp.dto.supplier.SupplierResponse;
import com.onlineShop.bootcamp.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public static SupplierResponse toSupplierResponse(Supplier supplier){

        return new SupplierResponse(
                supplier.getId(),
                supplier.getName()
        );
    }

}
