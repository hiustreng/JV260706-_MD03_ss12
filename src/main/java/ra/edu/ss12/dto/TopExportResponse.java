package ra.edu.ss12.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopExportResponse {
    private String topSupplyName;
    private Long totalExportQuantity;
}
