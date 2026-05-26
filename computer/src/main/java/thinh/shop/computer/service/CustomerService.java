package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    public CustomerRepository customerRepository;

    public void save(Customer customer) {
        customerRepository.save(customer);
    }
}
