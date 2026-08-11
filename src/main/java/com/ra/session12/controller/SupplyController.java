package com.ra.session12.controller;

import com.ra.session12.dto.request.SupplyCreateDTO;
import com.ra.session12.dto.request.SupplyUpdateDTO;
import com.ra.session12.dto.response.ApiResponse;
import com.ra.session12.model.entity.Supply;
import com.ra.session12.service.SupplyService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplies")
public class SupplyController {

    private final SupplyService supplyService;

    @PostMapping
    public ResponseEntity<ApiResponse<Supply>> createSupply(@Valid @RequestBody SupplyCreateDTO dto) {
        Supply supply = supplyService.createSupply(dto);

        ApiResponse<Supply> response = ApiResponse.<Supply>builder()
                .status("success")
                .code(HttpStatus.CREATED.value())
                .message("Tạo vật tư thành công")
                .data(supply)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Supply>> updateSupply(@PathVariable Long id,
                                                            @RequestBody Map<String, Object> requestBody) {
        // Kiểm tra field cấm TRƯỚC khi xử lý gì khác
        if (requestBody.containsKey("id") || requestBody.containsKey("quantity")) {
            log.warn("Phát hiện Client cố tình gửi dữ liệu cấm (id/quantity) khi cập nhật vật tư ID: {}", id);

            ApiResponse<Supply> response = ApiResponse.<Supply>builder()
                    .status("error")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Không được phép cập nhật trường id hoặc quantity")
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        SupplyUpdateDTO dto = SupplyUpdateDTO.builder()
                .name((String) requestBody.get("name"))
                .specification((String) requestBody.get("specification"))
                .provider((String) requestBody.get("provider"))
                .build();

        Supply updated = supplyService.updateSupply(id, dto);

        ApiResponse<Supply> response = ApiResponse.<Supply>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("Cập nhật vật tư thành công")
                .data(updated)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}