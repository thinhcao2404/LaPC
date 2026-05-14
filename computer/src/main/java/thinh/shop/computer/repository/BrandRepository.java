package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
