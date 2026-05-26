package thinh.shop.computer.service;

import org.springframework.stereotype.Service;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.dto.request.RegisterRequest;
import thinh.shop.computer.entity.Role;
import thinh.shop.computer.repository.AccountRepository;
import thinh.shop.computer.repository.CustomerRepository;
import thinh.shop.computer.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewCustomer(RegisterRequest dto) throws Exception {

        if (accountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }
        Account account = new Account();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");
        if (customerRole == null) {
            customerRole = new Role();
            customerRole.setName("ROLE_CUSTOMER");
            roleRepository.save(customerRole);
        }
        account.getRoles().add(customerRole);

        Account savedAccount = accountRepository.save(account);

        Customer customer = new Customer();
        customer.setFullName(dto.getFullName());
        customer.setPhone(dto.getPhone());
        customer.setAccount(savedAccount);

        customerRepository.save(customer);
    }

    public Account  findByUsername(String username) throws Exception {
        return accountRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Không tìm thấy user"));
    }

    public void save(Account account) {
        accountRepository.save(account);
    }
    public long countCustomer() {
        return accountRepository.count();
    }
    public List<Account> getAll(){
        return accountRepository.findAll();
    }
    public Account getAccount(Long id){
        return accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản"));
    }
}
