package thinh.shop.computer.dto.response;

import lombok.Data;


@Data
public class OrderDetailResponse {
    private Long id;
    private Integer quantity;
    private Double price;
    private String productName;
    private String mainImage;
    private String attributesString;
}
