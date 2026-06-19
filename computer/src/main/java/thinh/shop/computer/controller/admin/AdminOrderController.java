package thinh.shop.computer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.dto.response.OrderResponse;
import thinh.shop.computer.service.OrderService;

import java.util.List;

@Controller
public class AdminOrderController {
    @Autowired
    public OrderService orderService;

    @GetMapping("/admin/orders")
    public String manageOrders(Model model){
        List<OrderResponse> allOrders =orderService.getAllOrdersForAdmin();
        model.addAttribute("orders",allOrders);
                return "admin/admin-orders";
    }
    @GetMapping("/admin/order-detail")
    public String showOrderDetails(@RequestParam("id") Long id, Model model){
        OrderResponse  orderResponse = orderService.getOrder(id);
        model.addAttribute("order",orderResponse);
        return "admin/order-detail";
    }
    @PostMapping("/admin/update-order-status")
    public String updateOrderStatus(@RequestParam("id") Long id, @RequestParam("newStatus") String status){
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/order-detail?id=" + id;
    }
}
