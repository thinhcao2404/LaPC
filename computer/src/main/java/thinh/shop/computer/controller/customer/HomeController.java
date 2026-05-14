package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.dto.CategoryHomeDTO;
import thinh.shop.computer.entity.Brand;
import thinh.shop.computer.entity.Category;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.repository.BrandRepository;
import thinh.shop.computer.repository.CategoryRepository;
import thinh.shop.computer.repository.ProductRepository;
import thinh.shop.computer.service.CartService;
import thinh.shop.computer.service.CategoryService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    @Autowired
    private CartService cartService;
    public HomeController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandRepository brandRepository;
    @GetMapping("/")
    public String home(@RequestParam(name = "brandId", required = false) Long brandId,Model model, Principal principal) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categoryList",categories);

        List<Brand>  brands = brandRepository.findAll();
        model.addAttribute("brands",brands);

        if (principal != null) {
            try {
                int cartCount = cartService.getCartByUsername(principal.getName()).getCartItem().size();
                model.addAttribute("calculatedTotal", cartCount);
            } catch (Exception e) {
                model.addAttribute("calculatedTotal", 0);
            }
        } else {
            model.addAttribute("calculatedTotal", 0);
        }

        model.addAttribute("selectedBrandId",brandId);

        List<CategoryHomeDTO> featuredCategories = categoryService.getCategoriesWithTopProducts();
        model.addAttribute("featuredCategories",featuredCategories);
        return "customer/index";
    }
    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword,Model model) {
        List<Product> searchProducts = productRepository.findByNameContainingIgnoreCase(keyword);
        model.addAttribute("productList",searchProducts);
        model.addAttribute("keyword",keyword);
        model.addAttribute("categoryList",categoryRepository.findAll());
        return "customer/search";
    }

}
