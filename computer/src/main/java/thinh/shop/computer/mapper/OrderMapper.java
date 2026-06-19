package thinh.shop.computer.mapper;

import org.springframework.stereotype.Component;
import thinh.shop.computer.dto.response.OrderDetailResponse;
import thinh.shop.computer.dto.response.OrderResponse;
import thinh.shop.computer.entity.Order;
import thinh.shop.computer.entity.OrderDetail;
import thinh.shop.computer.entity.ProductVariant;
import thinh.shop.computer.entity.VariantAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderResponse toResponse(Order entity){
        if(entity == null){
            return null;
        }
        OrderResponse response = new OrderResponse();

        response.setId(entity.getId());
        response.setOrderDate(entity.getOrderDate());
        response.setCustomerName(entity.getCustomerName());
        response.setCustomerEmail(entity.getCustomerEmail());
        response.setShippingAddress(entity.getShippingAddress());
        response.setStatus(entity.getStatus());
        response.setNote(entity.getNote());
        response.setTotalAmount(entity.getTotalAmount());

        if(entity.getOrderDetails() != null){
            List<OrderDetailResponse> detailResponses = new ArrayList<>();
            for(OrderDetail dRes : entity.getOrderDetails()){
                OrderDetailResponse detailResponse = new OrderDetailResponse();
                detailResponse.setId(dRes.getId());
                detailResponse.setQuantity(dRes.getQuantity());
                detailResponse.setPrice(dRes.getPrice());

                if(dRes.getVariants() != null){
                    ProductVariant variant = dRes.getVariants();

                    if(variant.getProduct() != null){
                        detailResponse.setProductName(variant.getProduct().getName());
                        detailResponse.setMainImage(variant.getProduct().getMainImage());
                    }

                    if(variant.getAttributes() != null && !variant.getAttributes().isEmpty()){
                        String attrString = variant.getAttributes().stream()
                                .map(VariantAttribute::getValue)
                                .collect(Collectors.joining(" - "));
                        detailResponse.setAttributesString(attrString);
                    }
                }
                detailResponses.add(detailResponse);
            }
            response.setOrderDetails(detailResponses);
        }

        return response;
    }
}
