package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
