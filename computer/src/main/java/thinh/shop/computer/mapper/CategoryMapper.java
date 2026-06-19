package thinh.shop.computer.mapper;

import org.springframework.stereotype.Component;
import thinh.shop.computer.dto.response.CategoryResponse;
import thinh.shop.computer.entity.Category;

@Component
public class CategoryMapper {
    public CategoryResponse toResponse(Category entity) {
        if (entity == null) {
            return null;
        }
        CategoryResponse dto = new CategoryResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
