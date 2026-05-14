package thinh.shop.computer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import thinh.shop.computer.service.*;


@Controller
public class AdminController {
    @Autowired
    public OrderService orderService;
    @Autowired
    public ProductService productService;
    @Autowired
    public AccountService accountService;

    @GetMapping("/admin")
    public String admin(Model model) {
        long totalOrders = orderService.countOrders();
        long totalProducts = productService.countProduct();
        long totalCustomers = accountService.countCustomer();
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalCustomers", totalCustomers);
        return "admin/dashboard";
    }
}
