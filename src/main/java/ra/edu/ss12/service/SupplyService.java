package ra.edu.ss12.service;

import ra.edu.ss12.dto.*;
import ra.edu.ss12.model.Supply;

import java.util.List;

public interface SupplyService {
    Supply createSupply(SupplyCreateRequest req);
    Supply updateSupply(Long id, SupplyUpdateRequest req);
    void deleteSupply(Long id);
    List<Supply> getAllActiveSupplies();
    List<Supply> searchSuppliesByName(String name);
    void exportSupply(Long id, Integer amount);
    void importSupply(Long id, Integer amount);
    List<DailyExportStatisticDTO> getDailyExportStatistics();
    TopExportResponse getTopExportStatistic();
}