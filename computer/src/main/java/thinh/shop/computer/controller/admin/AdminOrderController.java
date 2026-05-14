package thinh.shop.computer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.entity.Order;
import thinh.shop.computer.service.OrderService;

import java.util.List;

@Controller
public class AdminOrderController {
    @Autowired
    public OrderService orderService;
    @GetMapping("/admin/orders")
    public String manageOrders(Model model){
        List<Order> allOrders =orderService.getAllOrders();
        model.addAttribute("orders",allOrders);
                return "admin/admin-orders";
    }
    @GetMapping("/admin/order-detail")
    public String showOrderDetails(@RequestParam("id") Long id, Model model){
        Order oder = orderService.getOrder(id);
        model.addAttribute("order",oder);
        return "admin/order-detail";
    }
    @PostMapping("/admin/update-order-status")
    public String updateOrderStatus(@RequestParam("id") Long id, @RequestParam("newStatus") String status){
        Order order = orderService.getOrder(id);
        order.setStatus(status);
        orderService.save(order);
        return "redirect:admin/orders";
    }
}
