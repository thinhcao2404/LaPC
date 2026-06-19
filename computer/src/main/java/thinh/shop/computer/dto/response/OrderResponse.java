package thinh.shop.computer.dto.response;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse{
    private Long id;
    private LocalDateTime orderDate;
    private String status;
    private String customerName;
    private String shippingPhone;
    private String shippingAddress;
    private String customerEmail;
    private Double totalAmount;
    private String note;

    private List<OrderDetailResponse> orderDetails;
}
