package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> id(Long id);
}
