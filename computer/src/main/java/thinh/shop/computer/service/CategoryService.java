package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thinh.shop.computer.dto.response.CategoryResponse;
import thinh.shop.computer.entity.Category;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.repository.CategoryRepository;
import thinh.shop.computer.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    public CategoryRepository categoryRepository;

    @Autowired
    public ProductRepository productRepository;

    public List<Category> getAllCategory(){
        return  categoryRepository.findAll();
    }

    public List<CategoryResponse> getCategoriesWithTopProducts() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> dtoList = new ArrayList<>();

        for (Category cat : categories) {
            List<Product> top5Products = productRepository.findTop5ByCategory_IdOrderByIdDesc(cat.getId());

            if (!top5Products.isEmpty()) {
                CategoryResponse dto = new CategoryResponse(cat.getId(), cat.getName(), top5Products);
                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
    public Optional<Category> findById(long id){
        return categoryRepository.findById(id);
    }
}
