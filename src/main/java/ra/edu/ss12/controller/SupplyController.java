package ra.edu.ss12.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.ss12.dto.*;
import ra.edu.ss12.model.Supply;
import ra.edu.ss12.service.SupplyService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/supplies")
@RequiredArgsConstructor
public class SupplyController {

    private final SupplyService supplyService;

    // Chức năng 1: Thêm mới vật tư y tế
    @PostMapping
    public ResponseEntity<Supply> createSupply(@Valid @RequestBody SupplyCreateRequest request) {
        Supply created = supplyService.createSupply(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Chức năng 2: Cập nhật thông tin vật tư
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Supply>> updateSupply(
            @PathVariable Long id,
            @RequestBody SupplyUpdateRequest request) {
        Supply updated = supplyService.updateSupply(id, request);
        return ResponseEntity.ok(ApiResponse.<Supply>builder()
                .message("Cập nhật thông tin vật tư thành công")
                .status(true)
                .data(updated)
                .build());
    }

    // Chức năng 3: Xóa vật tư (Xóa mềm)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
        supplyService.deleteSupply(id);
        return ResponseEntity.noContent().build();
    }

    // Chức năng 4: Hiển thị danh sách vật tư hiện có
    @GetMapping
    public ResponseEntity<ApiResponse<List<Supply>>> getAllActiveSupplies() {
        List<Supply> supplies = supplyService.getAllActiveSupplies();
        return ResponseEntity.ok(ApiResponse.<List<Supply>>builder()
                .message("Lấy danh sách vật tư thành công")
                .status(true)
                .data(supplies)
                .build());
    }

    // Chức năng 5: Tìm kiếm vật tư theo tên (gần đúng)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Supply>>> searchSupplies(@RequestParam String name) {
        List<Supply> supplies = supplyService.searchSuppliesByName(name);
        return ResponseEntity.ok(ApiResponse.<List<Supply>>builder()
                .message("Tìm kiếm hoàn tất")
                .status(true)
                .data(supplies)
                .build());
    }

    // Chức năng 6: Xuất kho
    @PatchMapping("/{id}/export")
    public ResponseEntity<ApiResponse<Object>> exportSupply(
            @PathVariable Long id,
            @Valid @RequestBody InventoryAdjustmentRequest request) {
        supplyService.exportSupply(id, request.getAmount());
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Xuất kho thành công")
                .status(true)
                .build());
    }

    // Chức năng 7: Nhập kho
    @PatchMapping("/{id}/import")
    public ResponseEntity<ApiResponse<Object>> importSupply(
            @PathVariable Long id,
            @Valid @RequestBody InventoryAdjustmentRequest request) {
        supplyService.importSupply(id, request.getAmount());
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Nhập kho thành công")
                .status(true)
                .build());
    }

    // Chức năng 8: Thống kê vật tư xuất trong ngày
    @GetMapping("/statistics/daily-export")
    public ResponseEntity<ApiResponse<List<DailyExportStatisticDTO>>> getDailyExportStatistics() {
        List<DailyExportStatisticDTO> stats = supplyService.getDailyExportStatistics();
        return ResponseEntity.ok(ApiResponse.<List<DailyExportStatisticDTO>>builder()
                .message("Thống kê xuất kho trong ngày")
                .status(true)
                .data(stats)
                .build());
    }

    // Chức năng 9: Thống kê vật tư xuất kho nhiều nhất
    @GetMapping("/statistics/top-export")
    public ResponseEntity<TopExportResponse> getTopExportStatistic() {
        TopExportResponse top = supplyService.getTopExportStatistic();
        return ResponseEntity.ok(top);
    }
}