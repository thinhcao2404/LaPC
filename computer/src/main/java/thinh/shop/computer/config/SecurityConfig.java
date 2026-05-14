package thinh.shop.computer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private CustomSuccessHandler customSuccessHandler;
    @Autowired
    private CustomFailureHandler customerFailureHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // PHÂN QUYỀN ĐƯỜNG DẪN
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        .requestMatchers("/", "/register", "/login", "/products/**").permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers("/cart/**", "/checkout/**", "/profile/**").hasRole("CUSTOMER")

                        .anyRequest().authenticated()
                )

                // 2. CẤU HÌNH TRANG ĐĂNG NHẬP
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler)
                        .failureHandler(customerFailureHandler)
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("Thinh_Pc_2404")
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(7*24*60*60)
                )

                // 3. CẤU HÌNH ĐĂNG XUẤT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                );

        return http.build();
    }
}