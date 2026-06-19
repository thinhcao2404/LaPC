    package thinh.shop.computer.mapper;

    import org.springframework.stereotype.Component;
    import thinh.shop.computer.dto.request.AttributeRequest;
    import thinh.shop.computer.dto.request.ProductCreateRequest;
    import thinh.shop.computer.dto.request.ProductUpdateRequest;
    import thinh.shop.computer.dto.request.VariantRequest;
    import thinh.shop.computer.dto.response.ProductResponse;
    import thinh.shop.computer.entity.*;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Objects;

    @Component
    public class ProductMapper {

        public void updateEntityFromRequest(ProductUpdateRequest request,
                           Product existingProduct,
                           Category category,
                           Brand brand) {
            if(request == null || existingProduct == null) return;

            existingProduct.setName(request.getName());
            existingProduct.setDescription(request.getDescription());
            if (request.getMainImage() != null && !request.getMainImage().isEmpty()) {
                existingProduct.setMainImage(request.getMainImage());
            }
            if (category != null) {
                existingProduct.setCategory(category);
            }
            if (brand != null) {
                existingProduct.setBrand(brand);
            }
            if (request.getVariants() != null) {
                for (VariantRequest vReq : request.getVariants()) {

                    if (vReq.getId() != null) {
                        // CẬP NHẬT CẤU HÌNH CŨ
                        ProductVariant existingVariant = existingProduct.getVariants().stream()
                                .filter(v -> v.getId().equals(vReq.getId()))
                                .findFirst()
                                .orElse(null);

                        if (existingVariant != null) {
                            existingVariant.setSku(vReq.getSku());
                            existingVariant.setPrice(vReq.getPrice());
                            existingVariant.setStock(vReq.getStock());

                            // Gọi hàm phụ để cập nhật RAM, Ổ cứng...
                            updateAttributes(vReq, existingVariant);
                        }
                    } else {
                        // THÊM CẤU HÌNH HOÀN TOÀN MỚI
                        ProductVariant newVariant = new ProductVariant();
                        newVariant.setSku(vReq.getSku());
                        newVariant.setPrice(vReq.getPrice());
                        newVariant.setStock(vReq.getStock());

                        // Móc nối 2 chiều BẮT BUỘC để JPA lưu khóa ngoại (Tránh mồ côi)
                        newVariant.setProduct(existingProduct);

                        // Xử lý các thuộc tính động cho cấu hình mới này
                        if (vReq.getAttributes() != null) {
                            for (AttributeRequest attrReq : vReq.getAttributes()) {
                                VariantAttribute newAttr = new VariantAttribute();
                                newAttr.setName(attrReq.getName());
                                newAttr.setValue(attrReq.getValue());

                                // Móc nối 2 chiều BẮT BUỘC
                                newAttr.setVariant(newVariant);
                                newVariant.getAttributes().add(newAttr);
                            }
                        }
                        // Bơm cấu hình mới vào sản phẩm
                        existingProduct.getVariants().add(newVariant);
                    }
                }
            }
        }

        private void updateAttributes(VariantRequest vReq, ProductVariant existingVariant) {
            if (vReq.getAttributes() != null) {
                for (AttributeRequest attrReq : vReq.getAttributes()) {
                    if (attrReq.getId() != null) {
                        // Ghi đè thuộc tính cũ
                        VariantAttribute existingAttr = existingVariant.getAttributes().stream()
                                .filter(a -> a.getId().equals(attrReq.getId()))
                                .findFirst()
                                .orElse(null);

                        if (existingAttr != null) {
                            existingAttr.setName(attrReq.getName());
                            existingAttr.setValue(attrReq.getValue());
                        }
                    } else {
                        // Thêm thuộc tính mới
                        VariantAttribute newAttr = new VariantAttribute();
                        newAttr.setName(attrReq.getName());
                        newAttr.setValue(attrReq.getValue());

                        newAttr.setVariant(existingVariant);
                        existingVariant.getAttributes().add(newAttr);
                    }
                }
            }
        }

        public ProductUpdateRequest toUpdateRequestFromEntity(Product entity) {
            if(entity == null) return null;
            ProductUpdateRequest dto = new ProductUpdateRequest();
            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            dto.setMainImage(entity.getMainImage());
            dto.setId(entity.getId());
            if(entity.getCategory() != null) {
                dto.setCategory(entity.getCategory().getId());
            }
            if(entity.getBrand() != null) {
                dto.setBrand(entity.getBrand().getId());
            }
            if (entity.getVariants() != null && !entity.getVariants().isEmpty()) {
                List<VariantRequest> variantRequests = new ArrayList<>();
                for(ProductVariant variant : entity.getVariants()) {
                    if (variant.getActive() != null && !variant.getActive()) {
                        continue;
                    }
                    VariantRequest vReq = new VariantRequest();
                    vReq.setId(variant.getId());
                    vReq.setSku(variant.getSku());
                    vReq.setPrice(variant.getPrice());
                    vReq.setStock(variant.getStock());

                    if (variant.getAttributes() != null && !variant.getAttributes().isEmpty()) {
                        List<AttributeRequest> attributeRequests = new ArrayList<>();
                        for(VariantAttribute attrRequests : variant.getAttributes()) {
                            AttributeRequest attrReq = new AttributeRequest();
                            attrReq.setId(attrRequests.getId());
                            attrReq.setName(attrRequests.getName());
                            attrReq.setValue(attrRequests.getValue());
                            attributeRequests.add(attrReq);
                        }
                        // Gắn danh sách thuộc tính vào cấu hình
                        vReq.setAttributes(attributeRequests);
                    }
                    // Gắn cấu hình vào danh sách tổng
                    variantRequests.add(vReq);
                }
                // Gắn danh sách tổng vào DTO
                dto.setVariants(variantRequests);
            }
            return dto;
        }

        public Product toEntityFromCreateRequest(ProductCreateRequest request,
                                                 Category category,
                                                 Brand brand) {
            if(request == null) return null;
            Product product = new Product();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setMainImage(request.getMainImage());
            product.setCategory(category);
            product.setBrand(brand);

            if (request.getVariants() != null){
                for(VariantRequest vReq : request.getVariants()) {
                    ProductVariant variant = new ProductVariant();
                    variant.setSku(vReq.getSku());
                    variant.setPrice(vReq.getPrice());
                    variant.setStock(vReq.getStock());

                    variant.setProduct(product);

                    if(vReq.getAttributes() != null) {
                        for(AttributeRequest attrReq : vReq.getAttributes()) {
                            VariantAttribute vAttr = new VariantAttribute();
                            vAttr.setId(attrReq.getId());
                            vAttr.setName(attrReq.getName());
                            vAttr.setValue(attrReq.getValue());

                            vAttr.setVariant(variant);

                            variant.getAttributes().add(vAttr);
                        }
                    }
                    product.getVariants().add(variant);
                }
            }
            return product;
        }

        public ProductResponse toResponse(Product entity) {
            if(entity == null) return null;
            ProductResponse dto = new ProductResponse();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setMainImage(entity.getMainImage());

            if(entity.getBrand() != null) {
                dto.setBrandName(entity.getBrand().getName());
            }
            if (entity.getVariants() != null && !entity.getVariants().isEmpty()) {
                // Lọc qua tất cả các cấu hình, lấy ra giá nhỏ nhất
                Double minPrice = entity.getVariants().stream()
                        .filter(v -> v.getActive() == null || v.getActive()) // Chỉ lấy hàng đang bán
                        .map(ProductVariant::getPrice)
                        .filter(Objects::nonNull)
                        .min(Double::compareTo)
                        .orElse(0.0);

                dto.setPrice(minPrice);
            } else {
                dto.setPrice(0.0);
            }
            return  dto;
        }
    }
