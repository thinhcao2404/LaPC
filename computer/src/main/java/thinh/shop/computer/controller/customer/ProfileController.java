package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thinh.shop.computer.dto.response.CustomerResponse;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.entity.Customer;
import thinh.shop.computer.service.AccountService;
import thinh.shop.computer.service.CustomerService;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Controller
public class ProfileController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) throws Exception {
        String username = principal.getName();
        CustomerResponse customerResponse = customerService.getProfileByUsername(username);
        model.addAttribute("customer",customerResponse);
        return "customer/profile";
    }
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("customer") CustomerResponse updateData,
                                Principal principal,
                                RedirectAttributes redirectAttributes) throws Exception {
        String username = principal.getName();
        try{
            customerService.updateProfile(username, updateData);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại.");        }
        return "redirect:/profile?success";
    }

}
