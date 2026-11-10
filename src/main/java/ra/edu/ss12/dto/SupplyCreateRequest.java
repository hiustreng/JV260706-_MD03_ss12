package ra.edu.ss12.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyCreateRequest {

    @NotBlank(message = "Tên vật tư không được để trống")
    private String name;

    private String provider;

    private String unit;

    private String specification;

    private Integer quantity;
}
