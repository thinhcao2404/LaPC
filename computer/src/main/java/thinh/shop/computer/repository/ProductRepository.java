package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByBrandId(Long brandId);

    List<Product> findTop5ByCategory_IdOrderByIdDesc(Long categoryId);
}
