package thinh.shop.computer.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class CartItemResponse {
    private Long variantId;

    private String productName;

    private String imageUrl;

    private List<String> attributes;

    private double price;

    private int quantity;

    private double subTotal;
}
