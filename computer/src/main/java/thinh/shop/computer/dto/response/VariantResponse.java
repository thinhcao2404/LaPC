package thinh.shop.computer.dto.response;

import lombok.Data;

@Data
public class VariantResponse {
    private Long id;
    private String sku;
    private Double price;
    private Integer stock;

    private String attributeString;
}