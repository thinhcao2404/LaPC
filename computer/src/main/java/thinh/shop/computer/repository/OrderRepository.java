package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerOrderByOrderDateDesc(Customer customer);
    @Query(value = "SELECT DATE_FORMAT(MIN(order_date), 'Tháng %m') AS monthLabel, SUM(total_amount) AS revenue " +
            "FROM orders " +
            "WHERE order_date >= DATE_SUB(CURDATE(), INTERVAL 5 MONTH) " +
            "AND status = 'COMPLETED' " +
            "GROUP BY YEAR(order_date), MONTH(order_date) " +
            "ORDER BY YEAR(order_date) ASC, MONTH(order_date) ASC", nativeQuery = true)
    List<Object[]> getRevenueLast6Months();
}
