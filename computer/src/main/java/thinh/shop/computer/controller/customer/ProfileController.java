package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.service.AccountService;
import thinh.shop.computer.service.CustomerService;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class ProfileController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) throws Exception {
        String username = principal.getName();
        Account account= accountService.findByUsername(username);
        Customer customer =  account.getCustomer();
        model.addAttribute("customer", customer);
        return "customer/profile";
    }
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("customer") Customer updateData, Principal principal, RedirectAttributes redirectAttributes) throws Exception {
        Account account =accountService.findByUsername(principal.getName());
        Customer newCustomer = account.getCustomer();

        newCustomer.setFullName(updateData.getFullName());
        newCustomer.setAddress(updateData.getAddress());
        newCustomer.setPhone(updateData.getPhone());

        customerService.save(newCustomer);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");
        return "redirect:/profile?success";
    }

}
