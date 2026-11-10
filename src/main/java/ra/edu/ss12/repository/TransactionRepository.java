package ra.edu.ss12.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.ss12.dto.DailyExportStatisticDTO;
import ra.edu.ss12.model.Transaction;
import ra.edu.ss12.model.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT new ra.edu.ss12.dto.DailyExportStatisticDTO(t.supply.id, t.supply.name, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.type = :type AND t.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY t.supply.id, t.supply.name")
    List<DailyExportStatisticDTO> getDailyExportStatistics(@Param("type") TransactionType type,
                                                           @Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t.supply.name, SUM(t.amount) " +
            "FROM Transaction t " +
            "WHERE t.type = :type " +
            "GROUP BY t.supply.id, t.supply.name " +
            "ORDER BY SUM(t.amount) DESC")
    List<Object[]> findTopExportSupply(@Param("type") TransactionType type);
}