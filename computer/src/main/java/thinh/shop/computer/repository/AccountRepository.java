package thinh.shop.computer.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.dto.response.AccountResponse;
import thinh.shop.computer.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
   Optional<Account> findByUsername(String username);
}