package thinh.shop.computer.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import thinh.shop.computer.entity.Category;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.repository.CategoryRepository;
import thinh.shop.computer.repository.ProductRepository;

import java.util.List;

@Controller
public class CategoryController {

    public final ProductRepository productRepository;
    public final CategoryRepository categoryRepository;
    public CategoryController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    @GetMapping("/category/{id}")
    public String  category(Model model, @PathVariable Long id) {
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categoryList",categoryList);

        Category category =  categoryRepository.findById(id).orElse(null);
        model.addAttribute("category",category);

        if(category!=null){
            List<Product> productList = productRepository.findByCategory_Id(category.getId());
            model.addAttribute("productList",productList);
        }

        return "customer/category";

    }
}
