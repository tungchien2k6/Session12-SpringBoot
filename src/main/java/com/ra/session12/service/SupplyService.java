package com.ra.session12.service;

import com.ra.session12.dto.request.SupplyCreateDTO;
import com.ra.session12.dto.request.SupplyUpdateDTO;
import com.ra.session12.model.entity.Supply;

public interface SupplyService {
    Supply createSupply(SupplyCreateDTO dto);
    Supply updateSupply(Long id, SupplyUpdateDTO dto);
    void deleteSupply(Long id);
}