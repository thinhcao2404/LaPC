package thinh.shop.computer.mapper;

import org.springframework.stereotype.Component;
import thinh.shop.computer.dto.response.BrandResponse;
import thinh.shop.computer.entity.Brand;

@Component
public class BrandMapper {

    public BrandResponse toResponse(Brand entity) {
        if (entity == null) {
            return null;
        }

        BrandResponse dto = new BrandResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLogo(entity.getLogo());

        return dto;
    }
}