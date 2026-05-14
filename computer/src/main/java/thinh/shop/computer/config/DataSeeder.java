package thinh.shop.computer.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Role;
import thinh.shop.computer.repository.AccountRepository;
import thinh.shop.computer.repository.RoleRepository;

import java.util.Set;
import java.util.HashSet;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initDatabase(AccountRepository accountRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {
            Role adminRole  = roleRepository.findByName("ROLE_ADMIN");
            if(adminRole == null) {
                adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            if (!accountRepository.findByUsername("admin").isPresent()){
                Account adminAccount = new Account();
                adminAccount.setUsername("admin");
                adminAccount.setPassword(passwordEncoder.encode("123456"));
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                adminAccount.setRoles(roles);
                accountRepository.save(adminAccount);
                System.out.println("✅ Đã tạo tài khoản Admin mặc định thành công!");
            }
        };
    }
}