package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thinh.shop.computer.dto.response.CustomerResponse;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.mapper.CustomerMapper;
import thinh.shop.computer.repository.AccountRepository;
import thinh.shop.computer.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    public CustomerRepository customerRepository;
    @Autowired
    public CustomerMapper customerMapper;
    @Autowired
    public AccountRepository accountRepository;

    public CustomerResponse getProfileByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy tài khoản!"));

        return customerMapper.toDto(account.getCustomer());
    }

    @Transactional
    public void updateProfile(String username, CustomerResponse dto) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy tài khoản!"));

        Customer customer = account.getCustomer();

        if (customer == null) {
            customer = new Customer();
            customer.setAccount(account);
        }

        customerMapper.updateEntityFromDto(dto, customer);

        customerRepository.save(customer);
    }
}
