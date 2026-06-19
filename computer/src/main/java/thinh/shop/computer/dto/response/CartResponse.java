package thinh.shop.computer.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private List<CartItemResponse> cartItems;

    private int calculatedTotal;

    private double totalPrice;
}
