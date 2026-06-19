package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thinh.shop.computer.dto.response.BrandResponse;
import thinh.shop.computer.entity.Brand;
import thinh.shop.computer.mapper.BrandMapper;
import thinh.shop.computer.repository.BrandRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandMapper brandMapper;

    public List<BrandResponse> getAllBrand(){
        List<Brand> brand = brandRepository.findAll();
        return brand.stream()
                .map(brandMapper::toResponse)
                .collect(Collectors.toList());
    }
}
