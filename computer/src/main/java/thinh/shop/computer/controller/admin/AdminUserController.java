package thinh.shop.computer.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.entity.Account;
import thinh.shop.computer.service.AccountService;

@Controller
public class AdminUserController {
    @Autowired
    public AccountService accountService;
    @GetMapping("/admin/accounts")
    public String adminAccounts(Model model) {
        model.addAttribute("accounts",accountService.getAll());
        return "admin/accounts";
    }

    @PostMapping("/admin/accounts/toggle-status")
    public String toggleAccountStatus(@RequestParam("id") Long id) {


        Account account = accountService.getAccount(id);


        account.setActive(!account.isActive());


        accountService.save(account);

        return "redirect:admin/accounts";
    }
}
