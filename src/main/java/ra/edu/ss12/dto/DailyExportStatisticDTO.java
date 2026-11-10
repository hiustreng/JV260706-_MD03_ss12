package ra.edu.ss11.dto;

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
