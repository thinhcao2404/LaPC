package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thinh.shop.computer.repository.ProductVariantRepository;

@Service
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;
    public boolean existsBySku(String sku){
        return productVariantRepository.existsBySku(sku);
    }
}
