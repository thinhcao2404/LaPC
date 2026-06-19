package thinh.shop.computer.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.ProductVariant;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {
    boolean existsBySku(String sku);
    boolean existsBySkuAndIdNot(String sku, Long id);
    List<ProductVariant> findByProductIdAndActiveTrue(Long productId);
}
