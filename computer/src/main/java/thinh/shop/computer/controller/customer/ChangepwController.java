package thinh.shop.computer.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.entity.Account;
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
                                  Principal principal
    ) throws Exception {
        if(!newPassword.equals(confirmPassword)){
            return "redirect:/change-password?error";
        }
        String username = principal.getName();
        Account account = accountService.findByUsername(username);
        if(!passwordEncoder.matches(oldPassword, account.getPassword())){
            return "redirect:/change-password?error";
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        accountService.save(account);
        return "redirect:/change-password?success";
    }
}
