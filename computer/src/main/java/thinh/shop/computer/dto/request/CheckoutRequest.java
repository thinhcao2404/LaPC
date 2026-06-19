package thinh.shop.computer.dto.request;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String name;
    private String phone;
    private String email;
    private String address;
    private String note;
}
