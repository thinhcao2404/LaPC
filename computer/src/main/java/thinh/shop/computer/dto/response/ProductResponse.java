package thinh.shop.computer.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String mainImage;

    private Long categoryId;
    private String categoryName;
    private String brandName;

    private Double price;
    private String sku;
    private Integer totalStock;

    private List<VariantResponse> variants = new ArrayList<>();
}