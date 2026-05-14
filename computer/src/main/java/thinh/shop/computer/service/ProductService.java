package thinh.shop.computer.service;

import org.springframework.stereotype.Service;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository  productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(Long productId){
return productRepository.findById(productId).orElse(null);
}

    public List<Product> searchProducts(String keyword){
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    public long countProduct(){
        return productRepository.count();
    }

    public List<Product> allProduct(){
        return productRepository.findAll();
    }

    public void save(Product product){
        productRepository.save(product);
    }

}
