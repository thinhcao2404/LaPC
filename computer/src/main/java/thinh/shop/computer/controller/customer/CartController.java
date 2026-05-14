package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Cart;
import thinh.shop.computer.entity.CartItem;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.service.AccountService;
import thinh.shop.computer.service.CartService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam("variantId") Long variantId,
            @RequestParam("quantity") Integer quantity,
            Principal principal) throws Exception {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("status", "error");
            response.put("message", "Vui lòng đăng nhập để mua hàng!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
try {
    String username = principal.getName();
    Account account = accountService.findByUsername(username);
    Customer customer = account.getCustomer();

    cartService.addToCart(customer, variantId, quantity);

    Cart cart = cartService.getCartByUsername(username);
    int totalItems = 0;
    if (cart != null && cart.getCartItem() != null) {
        totalItems = cart.getCartItem().size();
    }

    response.put("status", "success");
    response.put("message", "Đã thêm vào giỏ hàng!");
    response.put("calculatedTotal", totalItems);
    return ResponseEntity.ok(response);
}
 catch(RuntimeException e){
     response.put("status", "error");
     response.put("message", e.getMessage());
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        Cart cart = cartService.getCartByUsername(username);

        if (cart != null) {
            model.addAttribute("cartItems", cart.getCartItem());
            double calculatedTotal = 0;
            for (CartItem item : cart.getCartItem()) {
                calculatedTotal += item.getQuantity() * item.getVariants().getPrice();
            }
            model.addAttribute("totalPrice", calculatedTotal);
            model.addAttribute("calculatedTotal", cart.getCartItem().size());
        } else {
            model.addAttribute("cartItems", new ArrayList<>());
            model.addAttribute("totalPrice", 0);
            model.addAttribute("calculatedTotal", 0);
        }

        return "customer/cart";
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCartQuantity(@RequestParam("variantId") Long variantId,
                                                  @RequestParam("quantity") int quantity,
                                                  Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            cartService.updateQuantity(principal.getName(), variantId, quantity);
            response.put("status", "success");
            return response;
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Lỗi: " + e.getMessage());
            return response;
        }
    }


    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("variantId") Long variantId, Principal principal) {
        if (principal != null) {
            cartService.removeCartItem(principal.getName(), variantId);
        }
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(Principal principal) {
        if (principal != null) {
            cartService.clearCart(principal.getName());
        }
        return "redirect:/cart";
    }
}