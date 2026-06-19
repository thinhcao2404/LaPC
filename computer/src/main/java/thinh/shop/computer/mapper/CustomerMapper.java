package thinh.shop.computer.mapper;

import org.springframework.stereotype.Component;
import thinh.shop.computer.dto.response.CustomerResponse;
import thinh.shop.computer.entity.Customer;

@Component
public class CustomerMapper {

    public CustomerResponse toDto(Customer entity) {
        CustomerResponse dto = new CustomerResponse();
        if (entity != null) {
            dto.setFullName(entity.getFullName());
            dto.setPhone(entity.getPhone());
            dto.setAddress(entity.getAddress());
        }
        return dto;
    }

    public void updateEntityFromDto(CustomerResponse dto, Customer entity) {
        if (dto != null && entity != null) {
            entity.setFullName(dto.getFullName());
            entity.setPhone(dto.getPhone());
            entity.setAddress(dto.getAddress());
        }
    }
}