package thinh.shop.computer.service;

import org.springframework.stereotype.Service;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public void save(Customer customer) {
        customerRepository.save(customer);
    }
}
