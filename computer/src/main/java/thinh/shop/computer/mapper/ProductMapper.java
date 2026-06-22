package thinh.shop.computer.mapper;

import org.springframework.stereotype.Component;
import thinh.shop.computer.dto.request.AttributeRequest;
import thinh.shop.computer.dto.request.ProductCreateRequest;
import thinh.shop.computer.dto.request.ProductUpdateRequest;
import thinh.shop.computer.dto.request.VariantRequest;
import thinh.shop.computer.dto.response.ProductResponse;
import thinh.shop.computer.dto.response.VariantResponse;
import thinh.shop.computer.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

                        updateAttributes(vReq, existingVariant);
                    }
                } else {
                    // THÊM CẤU HÌNH HOÀN TOÀN MỚI
                    ProductVariant newVariant = new ProductVariant();
                    newVariant.setSku(vReq.getSku());
                    newVariant.setPrice(vReq.getPrice());
                    newVariant.setStock(vReq.getStock());

                    // 🔥 BẬT TRẠNG THÁI ĐANG BÁN CHO CẤU HÌNH MỚI
                    newVariant.setActive(true);

                    newVariant.setProduct(existingProduct);

                    if (vReq.getAttributes() != null) {
                        for (AttributeRequest attrReq : vReq.getAttributes()) {
                            VariantAttribute newAttr = new VariantAttribute();
                            newAttr.setName(attrReq.getName());
                            newAttr.setValue(attrReq.getValue());

                            newAttr.setVariant(newVariant);
                            newVariant.getAttributes().add(newAttr);
                        }
                    }
                    existingProduct.getVariants().add(newVariant);
                }
            }
        }
    }

    private void updateAttributes(VariantRequest vReq, ProductVariant existingVariant) {
        if (vReq.getAttributes() != null) {
            for (AttributeRequest attrReq : vReq.getAttributes()) {
                if (attrReq.getId() != null) {
                    VariantAttribute existingAttr = existingVariant.getAttributes().stream()
                            .filter(a -> a.getId().equals(attrReq.getId()))
                            .findFirst()
                            .orElse(null);

                    if (existingAttr != null) {
                        existingAttr.setName(attrReq.getName());
                        existingAttr.setValue(attrReq.getValue());
                    }
                } else {
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
                    vReq.setAttributes(attributeRequests);
                }
                variantRequests.add(vReq);
            }
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

                variant.setActive(true);

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
        dto.setDescription(entity.getDescription());
        dto.setMainImage(entity.getMainImage());

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }

        if(entity.getBrand() != null) {
            dto.setBrandName(entity.getBrand().getName());
        }

        if (entity.getVariants() != null && !entity.getVariants().isEmpty()) {

            List<VariantResponse> variantResponses = entity.getVariants().stream()
                    .filter(v -> v.getActive() == null || v.getActive()) // Chỉ lấy hàng đang bán
                    .map(v -> {
                        VariantResponse vRes = new VariantResponse();
                        vRes.setId(v.getId());
                        vRes.setSku(v.getSku());
                        vRes.setPrice(v.getPrice());
                        vRes.setStock(v.getStock());

                        if (v.getAttributes() != null) {
                            String attrStr = v.getAttributes().stream()
                                    .map(VariantAttribute::getValue)
                                    .collect(Collectors.joining(" - "));
                            vRes.setAttributeString(attrStr);
                        }
                        return vRes;
                    })
                    .collect(Collectors.toList());

            dto.setVariants(variantResponses);

            // Tính giá Min
            Double minPrice = variantResponses.stream()
                    .map(VariantResponse::getPrice)
                    .filter(Objects::nonNull)
                    .min(Double::compareTo)
                    .orElse(0.0);
            dto.setPrice(minPrice);

            if (!variantResponses.isEmpty()) {
                VariantResponse firstVar = variantResponses.get(0);
                dto.setSku(firstVar.getSku());
                dto.setTotalStock(firstVar.getStock());
            }

        } else {
            dto.setPrice(0.0);
        }
        return dto;
    }
}