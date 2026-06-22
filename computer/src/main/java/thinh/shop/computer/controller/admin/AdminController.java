package thinh.shop.computer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import thinh.shop.computer.service.*;

import java.util.ArrayList;
import java.util.List;

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

        List<Object[]> revenueData = orderService.getMonthlyRevenueData();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (Object[] row : revenueData) {
            labels.add(row[0].toString());
            data.add(((Number) row[1]).doubleValue());
        }
        if (labels.isEmpty()) {
            labels = java.util.Arrays.asList("Chưa có dữ liệu");
            data = java.util.Arrays.asList(0.0);
        }

        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartData", data);

        return "admin/dashboard";
    }
}