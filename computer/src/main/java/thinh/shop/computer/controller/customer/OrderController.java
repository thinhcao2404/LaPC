package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.entity.Order;
import thinh.shop.computer.service.AccountService;
import thinh.shop.computer.service.OrderService;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    public AccountService accountService;
    @Autowired
    public OrderService orderService;
    @GetMapping("/order")
    public String viewOrder(Model model, Principal principal) throws Exception {
        String username = principal.getName();
        Account account = accountService.findByUsername(username);
        Customer customer = account.getCustomer();

        List<Order> orders = orderService.getOrderByCustomer(customer);
        model.addAttribute("orders", orders);
        return "customer/order";
    }
    @PostMapping("/checkout")
    public String checkout(Principal principal,
                           @RequestParam("name")String name,
                           @RequestParam("phone")String phone,
                           @RequestParam("address")String address,
                           @RequestParam(value="note",required=false)String note,
                           @RequestParam(value="email",required=false)String email,
                           RedirectAttributes redirectAttributes) throws Exception {
        try{
            String username = principal.getName();
            Account account = accountService.findByUsername(username);
            Customer customer = account.getCustomer();
            orderService.createOrder(customer,address,phone,note,name,email);
            return "redirect:/order";
        }catch(RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cart";
        }

    }
    @GetMapping("/order-detail")
    public String showOrderDetail(Model model,@RequestParam("id") Long id){
        Order order = orderService.getOrder(id);
        model.addAttribute("order",order);
        return "customer/order-detail";
    }
    @PostMapping("/cancel-order")
    public String cancelOrder(@RequestParam("id") Long id,RedirectAttributes redirectAttributes){
        try{
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Hủy đơn hàng thành công!");
        }catch(RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/order";
    }
}
