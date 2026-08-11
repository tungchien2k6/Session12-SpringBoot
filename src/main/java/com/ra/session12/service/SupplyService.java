package com.ra.session12.service;

import com.ra.session12.dto.request.SupplyCreateDTO;
import com.ra.session12.dto.request.SupplyUpdateDTO;
import com.ra.session12.model.entity.Supply;

import java.util.List;

public interface SupplyService {
    Supply createSupply(SupplyCreateDTO dto);
    Supply updateSupply(Long id, SupplyUpdateDTO dto);
    void deleteSupply(Long id);
    List<Supply> getAllSupplies();
}