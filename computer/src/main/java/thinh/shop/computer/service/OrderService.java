package thinh.shop.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thinh.shop.computer.entity.*;
import thinh.shop.computer.repository.*;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;

    public List<Order> getOrderByCustomer(Customer customer) {
        return orderRepository.findByCustomerOrderByOrderDateDesc(customer);
    }
    public Order getOrder(Long id){
        return orderRepository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy đơn hàng!"));
    }
    @Transactional
    public void createOrder(Customer customer,String address,String phone,String note,String name,String email){
        Cart cart = cartRepository.findByCustomer(customer).orElseThrow(()->new RuntimeException("Không tìm thấy giỏ hàng!"));
        List<CartItem> cartItems = cartItemRepository.getCartItemsByCart(cart);
        if(cartItems.isEmpty()){
            throw new RuntimeException("Giỏ hàng trống!");
        }

      Order order = new Order();
        order.setCustomer(customer);
        order.setCustomerName(name);
        order.setShippingAddress(address);
        order.setNote(note);
        order.setCustomerEmail(email);
        order.setShippingPhone(phone);
        order.setTotalAmount(0.0);
        order.setStatus("PENDING");
        order = orderRepository.save(order);

        Double totalAmount = 0.0;
        for(CartItem item : cartItems){
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);

            ProductVariant variant = item.getVariants();
            detail.setVariants(variant);

            int buyQuantity = item.getQuantity();
            detail.setQuantity(buyQuantity);

            double price = variant.getPrice();
            detail.setPrice(price);

            orderDetailRepository.save(detail);

            totalAmount +=(price*buyQuantity);
            variant.setStock(variant.getStock() - buyQuantity);
            productVariantRepository.save(variant);
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
    }
    @Transactional
    public void deleteOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy đơn hàng!"));
        if(!order.getStatus().equals("PENDING")){
            throw new RuntimeException("Chỉ có thể hủy đơn hàng đang chờ xác nhận!");
        }
        for(OrderDetail detail:order.getOrderDetails()){
            ProductVariant variant = detail.getVariants();
            int buyQuantity = detail.getQuantity();
            variant.setStock(variant.getStock() + buyQuantity);
            productVariantRepository.save(variant);
        }
        order.setStatus("CANCELED");
        orderRepository.save(order);
    }
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public long  countOrders(){
        return orderRepository.count();
    }
    public void save(Order order){
        orderRepository.save(order);
    }
}
