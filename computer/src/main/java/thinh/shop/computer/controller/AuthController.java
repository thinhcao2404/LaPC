package thinh.shop.computer.controller;

import thinh.shop.computer.dto.RegisterDTO;
import thinh.shop.computer.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {

        this.accountService = accountService;
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDTO());
        return "customer/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("registerDto") RegisterDTO dto, Model model) {
        try {
            accountService.registerNewCustomer(dto);
            return "redirect:/login?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "customer/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}