package ra.edu.ss11.dto;

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
