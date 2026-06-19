package thinh.shop.computer.mapper;

import org.springframework.stereotype.Component;
import thinh.shop.computer.dto.response.CartItemResponse;
import thinh.shop.computer.dto.response.CartResponse;
import thinh.shop.computer.entity.Cart;
import thinh.shop.computer.entity.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartItemResponse toItemResponse(CartItem entity) {
        if (entity == null) {
            return null;
        }
        CartItemResponse dto = new CartItemResponse();
        dto.setQuantity(entity.getQuantity());
        if(entity.getVariants() != null){
            dto.setVariantId(entity.getVariants().getId());
            Double safePrice = entity.getVariants().getPrice();
            if(safePrice == null){
                safePrice = 0.0;
            }
            dto.setPrice(safePrice);

            if(entity.getVariants().getProduct() != null){
                dto.setProductName(entity.getVariants().getProduct().getName());

                if(entity.getVariants().getImage() != null){
                    dto.setImageUrl(entity.getVariants().getImage());
                }else{
                    dto.setImageUrl(entity.getVariants().getProduct().getMainImage());
                }
            }
            if(entity.getVariants().getAttributes() != null){
                List<String> attributeStrings = entity.getVariants().getAttributes().stream()
                        .map(attr -> attr.getName() + ": " + attr.getValue())
                        .collect(Collectors.toList());
                dto.setAttributes(attributeStrings);
            }
        }
        int quantity = entity.getQuantity() != null ? entity.getQuantity() : 0;
        dto.setSubTotal(dto.getPrice()*quantity);

        return dto;
    }
    public CartResponse toResponse(Cart entity) {
        if (entity == null) {
            return null;
        }
        CartResponse dto = new CartResponse();
        List<CartItemResponse> itemResponses = new ArrayList<>();
        if (entity.getCartItem() != null && !entity.getCartItem().isEmpty()) {
            itemResponses = entity.getCartItem().stream()
                    .map(this::toItemResponse)
                    .collect(Collectors.toList());
        }
        dto.setCartItems(itemResponses);

        // Tính tổng số lượng sản phẩm đang có trong giỏ
        int calculatedTotal = itemResponses.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();
        dto.setCalculatedTotal(calculatedTotal);

        // Tính tổng tiền thanh toán của cả giỏ hàng
        double totalPrice = itemResponses.stream()
                .mapToDouble(CartItemResponse::getSubTotal)
                .sum();
        dto.setTotalPrice(totalPrice);

        return dto;
    }
}
