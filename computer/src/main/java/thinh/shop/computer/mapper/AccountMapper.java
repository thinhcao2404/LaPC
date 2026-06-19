package thinh.shop.computer.mapper;

import org.springframework.stereotype.Component;
import thinh.shop.computer.dto.request.RegisterRequest;
import thinh.shop.computer.dto.response.AccountResponse;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.entity.Role;

import java.util.stream.Collectors;

@Component
public class AccountMapper {
    public AccountResponse toResponse(Account entity) {
        if (entity == null) {
            return null;
        }
        AccountResponse dto = new AccountResponse();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setActive(entity.isActive());

        if(entity.getCustomer()!=null){
            dto.setFullName(entity.getCustomer().getFullName());
            dto.setPhone(entity.getCustomer().getPhone());
        }
        if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
            dto.setRoles(entity.getRoles().stream()
                    .map(Role::getName) // Chỉ nhặt thuộc tính 'name' của Role
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public Account  toEntity(RegisterRequest request) {
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setPhone(request.getPhone());
        account.setCustomer(customer);
        customer.setAccount(account);
        return account;
    }
}
