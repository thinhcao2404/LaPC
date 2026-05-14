package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}