package thinh.shop.computer.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VariantRequest {
    private Long id;
    private String sku;
    private Double price;
    private Integer stock;

    private List<AttributeRequest> attributes = new ArrayList<>();
}