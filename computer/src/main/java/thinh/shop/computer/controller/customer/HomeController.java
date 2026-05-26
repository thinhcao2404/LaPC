package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.dto.response.CategoryResponse;
import thinh.shop.computer.entity.Brand;
import thinh.shop.computer.entity.Category;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.service.BrandService;
import thinh.shop.computer.service.CartService;
import thinh.shop.computer.service.CategoryService;
import thinh.shop.computer.service.ProductService;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @GetMapping("/")
    public String home(@RequestParam(name = "brandId", required = false) Long brandId,
                       Model model,
                       Principal principal) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categoryList",categories);

        List<Brand>  brands = brandService.getAllBrand();
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

        List<CategoryResponse> featuredCategories = categoryService.getCategoriesWithTopProducts();
        model.addAttribute("featuredCategories",featuredCategories);
        return "customer/index";
    }
    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword,Model model) {
        List<Product> searchProducts = productService.searchProducts(keyword);
        model.addAttribute("productList",searchProducts);
        model.addAttribute("keyword",keyword);
        model.addAttribute("categoryList",categoryService.findAll());
        return "customer/search";
    }

}
