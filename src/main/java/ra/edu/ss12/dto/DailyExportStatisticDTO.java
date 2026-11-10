package ra.edu.ss12.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyExportStatisticDTO {
    private Long supplyId;
    private String supplyName;
    private Long totalExportToday;
}
