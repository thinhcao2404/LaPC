package thinh.shop.computer.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductCreateRequest {
    private String name;

    private Long brand;
    private Long category;

    private String mainImage;
    private String description;

    private List<VariantRequest> variants = new ArrayList<>();
}
