package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.dto.response.ProductResponse;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.entity.Product;
import thinh.shop.computer.service.AccountService;
import thinh.shop.computer.service.CartService;
import thinh.shop.computer.service.ProductService;

import java.security.Principal;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @GetMapping("/detail")
    public String viewProductDetail(@RequestParam("id") Long id, Model model, Principal principal) {
        ProductResponse productResponse = productService.getProductById(id);
        if(productResponse == null){
            return "redirect:/";
        }
        model.addAttribute("product", productResponse);

        if (principal != null) {
            try {
                int cartCount = cartService.getCartResponse(principal.getName()).getCalculatedTotal();
                model.addAttribute("calculatedTotal", cartCount);
            } catch (Exception e) {
                model.addAttribute("calculatedTotal", 0);
            }
        } else {
            model.addAttribute("calculatedTotal", 0);
        }
        return "customer/product-detail";
    }
}
