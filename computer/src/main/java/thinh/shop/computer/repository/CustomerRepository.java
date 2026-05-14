package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
