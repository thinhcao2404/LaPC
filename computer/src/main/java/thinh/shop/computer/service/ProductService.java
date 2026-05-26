package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    public ProductRepository  productRepository;

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

    public Optional<Product> findById(Long productId){
        return productRepository.findById(productId);
    }
    public void deleteById(Long productId){}

    public List<Product> findByCategory_Id(long categoryId){
        return productRepository.findByCategory_Id(categoryId);
    }
}
