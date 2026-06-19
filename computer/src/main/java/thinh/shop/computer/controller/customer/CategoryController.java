package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import thinh.shop.computer.dto.response.BrandResponse;
import thinh.shop.computer.dto.response.CategoryResponse;
import thinh.shop.computer.dto.response.ProductResponse;
import thinh.shop.computer.entity.Brand;
import thinh.shop.computer.entity.Category;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.repository.CategoryRepository;
import thinh.shop.computer.repository.ProductRepository;
import thinh.shop.computer.service.CategoryService;
import thinh.shop.computer.service.ProductService;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    public ProductService productService;
    @Autowired
    public CategoryService categoryService;

    @GetMapping("/category/{id}")
    public String  category(Model model, @PathVariable Long id) {

        List<CategoryResponse> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList",categoryList);

        CategoryResponse categoryResponse =  categoryService.getCategoryById(id);
        model.addAttribute("category",categoryResponse);

        if(categoryResponse!=null){
            List<ProductResponse> productList = productService.findByCategory_Id(categoryResponse.getId());
            model.addAttribute("productList",productList);
        }
        if (categoryResponse == null) {
            return "redirect:/";
        }
        List<BrandResponse> brandList = categoryResponse.getBrands();
        model.addAttribute("brandList",brandList);

        return "customer/category";

    }
}
