package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thinh.shop.computer.dto.CategoryHomeDTO;
import thinh.shop.computer.entity.Category;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.repository.CategoryRepository;
import thinh.shop.computer.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    public CategoryRepository categoryRepository;

    @Autowired
    public ProductRepository productRepository;

    public List<Category> getAllCategory(){
        return  categoryRepository.findAll();
    }

    public List<CategoryHomeDTO> getCategoriesWithTopProducts() {
        // 1. Lấy tất cả danh mục
        List<Category> categories = categoryRepository.findAll();
        List<CategoryHomeDTO> dtoList = new ArrayList<>();

        // 2. Lặp qua từng danh mục
        for (Category cat : categories) {
            // 3. Lấy 5 sản phẩm mới nhất của danh mục này
            List<Product> top5Products = productRepository.findTop5ByCategory_IdOrderByIdDesc(cat.getId());

            // 4. Chỉ hiển thị danh mục nếu nó có ít nhất 1 sản phẩm
            if (!top5Products.isEmpty()) {
                CategoryHomeDTO dto = new CategoryHomeDTO(cat.getId(), cat.getName(), top5Products);
                dtoList.add(dto);
            }
        }

        return dtoList;
    }
}
