package thinh.shop.computer.dto.request;

import lombok.Data;

@Data
public class CartUpdateRequest {
    private Long variantId;
    private int quantity;
}
