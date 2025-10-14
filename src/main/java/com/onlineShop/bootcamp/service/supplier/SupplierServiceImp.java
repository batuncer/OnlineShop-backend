package com.onlineShop.bootcamp.service.supplier;

import com.onlineShop.bootcamp.dto.supplier.SupplierRequest;
import com.onlineShop.bootcamp.dto.supplier.SupplierResponse;
import com.onlineShop.bootcamp.entity.Supplier;
import com.onlineShop.bootcamp.mapper.SupplierMapper;
import com.onlineShop.bootcamp.repository.SupplierRepository;
import com.onlineShop.bootcamp.service.order.OrderServiceImp;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImp implements SupplierService{

    private final SupplierRepository supplierRepository;
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);

    @Override
    public List<SupplierResponse> getAllSuppliers() {
        logger.info("Fetching all suppliers ");
        return supplierRepository.findAll().stream().map(SupplierMapper::toSupplierResponse).toList();
    }

    @Override
    public SupplierResponse createSupplier(SupplierRequest supplierRequest) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierRequest.getName());

        Supplier savedSupplier = supplierRepository.save(supplier);
        logger.info("Creating a supplier ");
        return SupplierMapper.toSupplierResponse(savedSupplier);
    }

}
