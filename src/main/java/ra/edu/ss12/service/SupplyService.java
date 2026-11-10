package ra.edu.ss11.service;

import ra.edu.ss11.dto.*;
import ra.edu.ss11.model.Supply;
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
