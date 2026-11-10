package ra.edu.ss11.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.ss11.dto.*;
import ra.edu.ss11.exception.CustomBadRequestException;
import ra.edu.ss11.exception.ResourceNotFoundException;
import ra.edu.ss11.model.*;
import ra.edu.ss11.repository.SupplyRepository;
import ra.edu.ss11.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepository supplyRepository;
    private final TransactionRepository transactionRepository;

    private static final Logger historyLog = LoggerFactory.getLogger("historyLogger");

    @Override
    public Supply createSupply(SupplyCreateRequest req) {
        Supply supply = Supply.builder()
                .name(req.getName())
                .provider(req.getProvider())
                .unit(req.getUnit())
                .specification(req.getSpecification())
                .quantity(0)
                .isDeleted(false)
                .build();

        Supply saved = supplyRepository.save(supply);
        log.info("Đã tạo mới vật tư: {} với ID: {}", saved.getName(), saved.getId());
        return saved;
    }

    @Override
    public Supply updateSupply(Long id, SupplyUpdateRequest req) {
        if (req.getId() != null || req.getQuantity() != null) {
            log.warn("Client cố tình gửi dữ liệu cấm (id: {}, quantity: {})", req.getId(), req.getQuantity());
            throw new CustomBadRequestException("Không được phép cập nhật ID hoặc số lượng (quantity) qua API này!");
        }

        Supply supply = supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư hoặc vật tư đã bị xóa với ID: " + id));

        if (req.getName() != null) supply.setName(req.getName());
        if (req.getSpecification() != null) supply.setSpecification(req.getSpecification());
        if (req.getProvider() != null) supply.setProvider(req.getProvider());

        Supply updated = supplyRepository.save(supply);
        log.info("Đã cập nhật thông tin vật tư ID: {}", id);
        return updated;
    }

    @Override
    public void deleteSupply(Long id) {
        Supply supply = supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư hoặc vật tư đã bị xóa trước đó với ID: " + id));

        supply.setIsDeleted(true);
        supplyRepository.save(supply);
        log.info("Đã xóa mềm thành công vật tư ID: {}", id);
    }

    @Override
    public List<Supply> getAllActiveSupplies() {
        List<Supply> list = supplyRepository.findByIsDeletedFalse();
        log.debug("Số lượng bản ghi vừa truy vấn được từ Database: {}", list.size());
        return list;
    }

    @Override
    public List<Supply> searchSuppliesByName(String name) {
        List<Supply> list = supplyRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);
        if (list.isEmpty()) {
            log.info("Không tìm thấy vật tư nào khớp với từ khóa: {}", name);
        }
        return list;
    }

    @Override
    @Transactional
    public void exportSupply(Long id, Integer amount) {
        Supply supply = supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư ID: " + id));

        if (supply.getQuantity() < amount) {
            log.error("Thất bại khi xuất kho ID {}: Yêu cầu [{}], hiện có [{}]", id, amount, supply.getQuantity());
            throw new CustomBadRequestException("Số lượng tồn kho không đủ để xuất");
        }

        supply.setQuantity(supply.getQuantity() - amount);
        supplyRepository.save(supply);

        Transaction tx = Transaction.builder()
                .supply(supply)
                .type(TransactionType.EXPORT)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .build();
        transactionRepository.save(tx);

        log.info("Xuất kho thành công ID {}: Số lượng {}", id, amount);
    }

    @Override
    @Transactional
    public void importSupply(Long id, Integer amount) {
        Supply supply = supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư ID: " + id));

        int oldQuantity = supply.getQuantity();
        supply.setQuantity(oldQuantity + amount);
        supplyRepository.save(supply);

        Transaction tx = Transaction.builder()
                .supply(supply)
                .type(TransactionType.IMPORT)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .build();
        transactionRepository.save(tx);

        historyLog.info("Nhập kho ID [{}], số lượng [+{}] , tồn cũ [{}]", id, amount, oldQuantity);
    }

    @Override
    public List<DailyExportStatisticDTO> getDailyExportStatistics() {
        long startTime = System.currentTimeMillis();
        log.info("Bắt đầu chạy thống kê vật tư đã xuất kho trong ngày lúc: {}", LocalDateTime.now());

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<DailyExportStatisticDTO> result = transactionRepository.getDailyExportStatistics(
                TransactionType.EXPORT, startOfDay, endOfDay);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Hoàn thành thống kê xuất kho trong ngày. Thời gian thực thi: {} ms", duration);
        return result;
    }

    @Override
    public TopExportResponse getTopExportStatistic() {
        List<Object[]> result = transactionRepository.findTopExportSupply(TransactionType.EXPORT);
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Chưa có dữ liệu giao dịch để thống kê");
        }

        Object[] topRow = result.get(0);
        String supplyName = (String) topRow[0];
        Long totalQuantity = ((Number) topRow[1]).longValue();

        return TopExportResponse.builder()
                .topSupplyName(supplyName)
                .totalExportQuantity(totalQuantity)
                .build();
    }
}
