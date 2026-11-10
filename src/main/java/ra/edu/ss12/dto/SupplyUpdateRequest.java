package ra.edu.ss12.dto;

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
