package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thinh.shop.computer.entity.Brand;
import thinh.shop.computer.repository.BrandRepository;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    public BrandRepository brandRepository;
    public List<Brand> getAllBrand(){
        return  brandRepository.findAll();
    }
}
