package ra.edu.ss11.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.ss11.model.Supply;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {
    List<Supply> findByIsDeletedFalse();
    Optional<Supply> findByIdAndIsDeletedFalse(Long id);
    List<Supply> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);
}
