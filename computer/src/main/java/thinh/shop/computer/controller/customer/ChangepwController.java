package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thinh.shop.computer.service.AccountService;

import java.security.Principal;

@Controller
public class ChangepwController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/change-password")
    public String FormchangePassword() {
        return "customer/change-password";
    }
    @PostMapping("/change-password")
    public String processChangepw(@RequestParam("oldPassword") String oldPassword,
                                  @RequestParam("newPassword") String newPassword,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal
    ) throws Exception {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            return "redirect:/change-password";
        }
        try {
            String username = principal.getName();
            accountService.changePassword(username, oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật mật khẩu thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/change-password";
    }
}
