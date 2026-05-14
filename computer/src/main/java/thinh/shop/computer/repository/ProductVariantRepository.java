package thinh.shop.computer.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {
    boolean existsBySku(String sku);
}
