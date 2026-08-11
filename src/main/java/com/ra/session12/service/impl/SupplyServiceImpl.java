package com.ra.session12.service.impl;

import com.ra.session12.dto.request.StockChangeDTO;
import com.ra.session12.dto.request.SupplyCreateDTO;
import com.ra.session12.dto.request.SupplyUpdateDTO;
import com.ra.session12.exception.InsufficientStockException;
import com.ra.session12.exception.ResourceNotFoundException;
import com.ra.session12.model.entity.Supply;
import com.ra.session12.model.entity.Transaction;
import com.ra.session12.model.entity.TransactionType;
import com.ra.session12.repository.SupplyRepository;
import com.ra.session12.repository.TransactionRepository;
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

    @Override
    public List<Supply> searchSuppliesByName(String name) {
        List<Supply> supplies = supplyRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);

        if (supplies.isEmpty()) {
            log.info("Không tìm thấy vật tư nào khớp với từ khóa: {}", name);
        }

        return supplies;
    }

    private final TransactionRepository transactionRepository; // thêm field này vào constructor injection

    @Override
    public Supply exportSupply(Long id, StockChangeDTO dto) {
        Supply supply = supplyRepository.findById(id)
                .filter(s -> !s.getIsDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với id: " + id));

        if (supply.getQuantity() < dto.getAmount()) {
            log.error("Thất bại khi xuất kho ID {}: Yêu cầu {}, hiện có {}", id, dto.getAmount(), supply.getQuantity());
            throw new InsufficientStockException("Số lượng tồn kho không đủ để xuất");
        }

        supply.setQuantity(supply.getQuantity() - dto.getAmount());
        Supply saved = supplyRepository.save(supply);

        Transaction transaction = Transaction.builder()
                .supply(saved)
                .type(TransactionType.EXPORT)
                .amount(dto.getAmount())
                .build();
        transactionRepository.save(transaction);

        log.info("Xuất kho thành công ID {}: số lượng {}, tồn còn lại {}", id, dto.getAmount(), saved.getQuantity());

        return saved;
    }
}