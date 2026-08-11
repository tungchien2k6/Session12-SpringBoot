package com.ra.session12.service.impl;

import com.ra.session12.dto.request.SupplyCreateDTO;
import com.ra.session12.dto.request.SupplyUpdateDTO;
import com.ra.session12.exception.ResourceNotFoundException;
import com.ra.session12.model.entity.Supply;
import com.ra.session12.repository.SupplyRepository;
import com.ra.session12.service.SupplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepository supplyRepository;

    @Override
    public Supply createSupply(SupplyCreateDTO dto) {
        Supply supply = Supply.builder()
                .name(dto.getName())
                .provider(dto.getProvider())
                .specification(dto.getSpecification())
                .unit(dto.getUnit())
                .build();

        Supply saved = supplyRepository.save(supply);

        log.info("Đã tạo mới vật tư: {} với ID: {}", saved.getName(), saved.getId());

        return saved;
    }

    @Override
    public Supply updateSupply(Long id, SupplyUpdateDTO dto) {
        Supply supply = supplyRepository.findById(id)
                .filter(s -> !s.getIsDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với id: " + id));

        if (dto.getName() != null) {
            supply.setName(dto.getName());
        }
        if (dto.getSpecification() != null) {
            supply.setSpecification(dto.getSpecification());
        }
        if (dto.getProvider() != null) {
            supply.setProvider(dto.getProvider());
        }

        Supply saved = supplyRepository.save(supply);
        log.info("Đã cập nhật vật tư ID: {}", id);
        return saved;
    }

    @Override
    public void deleteSupply(Long id) {
        Supply supply = supplyRepository.findById(id)
                .filter(s -> !s.getIsDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với id: " + id));

        supply.setIsDeleted(true);
        supplyRepository.save(supply);

        log.info("Đã xóa mềm vật tư ID: {}", id);
    }

    @Override
    public List<Supply> getAllSupplies() {
        List<Supply> supplies = supplyRepository.findByIsDeletedFalse();
        log.debug("Đã truy vấn được {} bản ghi vật tư đang hoạt động", supplies.size());
        return supplies;
    }
}