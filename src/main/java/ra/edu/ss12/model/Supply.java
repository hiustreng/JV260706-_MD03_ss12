package ra.edu.ss12.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supplies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String specification;

    private String provider;

    private String unit;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean isDeleted;
}
