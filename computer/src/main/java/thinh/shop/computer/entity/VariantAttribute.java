package thinh.shop.computer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "variant_attribute")
public class VariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_name", nullable = false)
    private String name;

    @Column(name = "attribute_value", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;
}