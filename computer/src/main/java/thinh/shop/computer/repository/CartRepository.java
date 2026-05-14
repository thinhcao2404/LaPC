package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thinh.shop.computer.entity.Cart;
import thinh.shop.computer.entity.Customer;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByCustomer(Customer customer);
}
