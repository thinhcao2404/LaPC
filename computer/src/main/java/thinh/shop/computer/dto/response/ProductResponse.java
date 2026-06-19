package thinh.shop.computer.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String mainImage;
    private String brandName;
    private Double price;
}
