package com.onlineShop.bootcamp.service.supplier;

import com.onlineShop.bootcamp.dto.supplier.SupplierRequest;
import com.onlineShop.bootcamp.dto.supplier.SupplierResponse;

import java.util.List;

public interface SupplierService {
    List<SupplierResponse> getAllSuppliers();
    SupplierResponse createSupplier(SupplierRequest supplierRequest);
}
