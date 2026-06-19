package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thinh.shop.computer.dto.response.AccountResponse;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.dto.request.RegisterRequest;
import thinh.shop.computer.entity.Role;
import thinh.shop.computer.mapper.AccountMapper;
import thinh.shop.computer.repository.AccountRepository;
import thinh.shop.computer.repository.CustomerRepository;
import thinh.shop.computer.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountMapper accountMapper;

    @Transactional
    public void registerNewCustomer(RegisterRequest dto) throws Exception {
        if (accountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }
        Account account = accountMapper.toEntity(dto);
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setActive(true);
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");
        if (customerRole == null) {
            throw new Exception("Lỗi hệ thống Server: Chưa cấu hình quyền Khách hàng!");
        }
        account.getRoles().add(customerRole);
        accountRepository.save(account);
    }

    public Account  findByUsername(String username) throws Exception {
        return accountRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("Không tìm thấy user"));
    }

    public long countCustomer() {
        return accountRepository.count();
    }

    public List<Account> getAll(){
        return accountRepository.findAll();
    }

    public List<AccountResponse> getAllAccountsForAdmin() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void toggleAccountStatus(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy tài khoản với ID " + id));
        account.setActive(!account.isActive());
        accountRepository.save(account);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Lỗi hệ thống: Không tìm thấy tài khoản!"));
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác!");
        }
        String hashedNewPassword = passwordEncoder.encode(newPassword);
        account.setPassword(hashedNewPassword);
        accountRepository.save(account);
    }
}
