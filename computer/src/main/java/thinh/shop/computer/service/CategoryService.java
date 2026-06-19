package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thinh.shop.computer.dto.response.CategoryResponse;
import thinh.shop.computer.dto.response.ProductResponse;
import thinh.shop.computer.entity.Category;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.mapper.CategoryMapper;
import thinh.shop.computer.mapper.ProductMapper;
import thinh.shop.computer.repository.CategoryRepository;
import thinh.shop.computer.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoriesWithTopProducts() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> dtoList = new ArrayList<>();

        for (Category cat : categories) {
            List<Product> top5Products = productRepository.findTop5ByCategory_IdOrderByIdDesc(cat.getId());

            if (!top5Products.isEmpty()) {
                CategoryResponse dto = categoryMapper.toResponse(cat);

                List<ProductResponse> productResponses = top5Products.stream()
                        .map(productMapper::toResponse)
                        .collect(Collectors.toList());

                dto.setProducts(productResponses);

                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    public CategoryResponse getCategoryById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Danh mục!"));

        return categoryMapper.toResponse(category);
    }
}
