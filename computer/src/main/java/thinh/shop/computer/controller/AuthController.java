package thinh.shop.computer.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestParam;
import thinh.shop.computer.dto.request.RegisterRequest;
import thinh.shop.computer.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import thinh.shop.computer.utils.JwtUtils;

@Controller
public class AuthController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterRequest());
        return "customer/register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("registerDto") RegisterRequest dto, Model model) {
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

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpServletResponse response,
                               Model model) {
        try{
            //kiểm tra mật khẩu
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            //lấy thông tin user
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //tạo token
            String jwtToken = jwtUtils.generateToken(userDetails);

            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            // cho token vào cookie
            Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
            jwtCookie.setPath("/");
            jwtCookie.setHttpOnly(true);

            if (isAdmin) {
                jwtCookie.setMaxAge(-1);
            } else {
                jwtCookie.setMaxAge(24 * 60 * 60);
            }

            response.addCookie(jwtCookie);

            if(isAdmin){
                return "redirect:/admin";
            }else{
                return  "redirect:/";
            }
        } catch (LockedException e){
            return "redirect:/login?locked";
        } catch (AuthenticationException e){
            return "redirect:/login?error";
        }
    }
}