package thinh.shop.computer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="brands")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    private String logo;

    @OneToMany(mappedBy = "brand",cascade = CascadeType.ALL)
    private List<Product> products;

    @ManyToMany(mappedBy = "brands")
    private List<Category> categories;
}
