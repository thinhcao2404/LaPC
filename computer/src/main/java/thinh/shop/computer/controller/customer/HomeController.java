package thinh.shop.computer.controller.customer;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.dto.response.BrandResponse;
import thinh.shop.computer.dto.response.CategoryResponse;
import thinh.shop.computer.dto.response.ProductResponse;
import thinh.shop.computer.service.BrandService;
import thinh.shop.computer.service.CartService;
import thinh.shop.computer.service.CategoryService;
import thinh.shop.computer.service.ProductService;
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
                       Authentication authentication) {

        int cartCount = 0;

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                return "redirect:/admin";
            }

            try {
                cartCount = cartService.getCartResponse(authentication.getName()).getCalculatedTotal();
            } catch (Exception e) {
                cartCount = 0;
            }
        }

        model.addAttribute("calculatedTotal", cartCount);

        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categoryList", categories);

        List<BrandResponse> brands = brandService.getAllBrand();
        model.addAttribute("brands", brands);

        model.addAttribute("selectedBrandId", brandId);

        List<CategoryResponse> featuredCategories = categoryService.getCategoriesWithTopProducts();
        model.addAttribute("featuredCategories", featuredCategories);

        return "customer/index";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam("keyword") String keyword,Model model) {
        List<ProductResponse> searchProducts = productService.searchProducts(keyword);
        model.addAttribute("productList",searchProducts);
        model.addAttribute("keyword",keyword);
        model.addAttribute("categoryList",categoryService.getAllCategories());
        return "customer/search";
    }

}
