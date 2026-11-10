package ra.edu.ss11.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyUpdateRequest {
    private Long id;
    private Integer quantity;
    private String name;
    private String specification;
    private String provider;
}
