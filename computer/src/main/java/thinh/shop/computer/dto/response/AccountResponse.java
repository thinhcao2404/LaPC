package thinh.shop.computer.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AccountResponse {
    private Long id;
    private String username;

    private String fullName;
    private String phone;

    private List<String> roles;

    private boolean active;
}
