package thinh.shop.computer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import thinh.shop.computer.entity.*;
import thinh.shop.computer.repository.AccountRepository;
import thinh.shop.computer.repository.CartItemRepository;
import thinh.shop.computer.repository.CartRepository;

import thinh.shop.computer.repository.ProductVariantRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    public CartRepository cartRepository;
    @Autowired
    public CartItemRepository cartItemRepository;
    @Autowired
    public ProductVariantRepository productVariantRepository;
    @Autowired
    public AccountRepository accountRepository;

   @Transactional
   public void addToCart(Customer customer, Long variantId, Integer quantity) {
       Cart cart = cartRepository.findByCustomer(customer)
               .orElseGet(() -> {
                   Cart newCart = new Cart();
                   newCart.setCustomer(customer);
                   return cartRepository.save(newCart);
               });


       ProductVariant variant = productVariantRepository.findById(variantId)
               .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình sản phẩm này!"));


       Optional<CartItem> existingItem = cartItemRepository.findByCartAndVariants(cart, variant);


       int currentInCart = existingItem.map(CartItem::getQuantity).orElse(0);


       int totalRequested = currentInCart + quantity;


       if (totalRequested > variant.getStock()) {
           String message = (currentInCart > 0)
                   ? "Bạn đã có " + currentInCart + " sản phẩm trong giỏ. Kho chỉ còn " + variant.getStock() + " cái, không thể thêm nữa!"
                   : "Rất tiếc, kho chỉ còn tối đa " + variant.getStock() + " sản phẩm!";
           throw new RuntimeException(message);
       }


       if (existingItem.isPresent()) {
           CartItem item = existingItem.get();
           item.setQuantity(totalRequested);
           cartItemRepository.save(item);
       } else {
           CartItem newItem = new CartItem();
           newItem.setCart(cart);
           newItem.setVariants(variant);
           newItem.setQuantity(quantity);
           cartItemRepository.save(newItem);
       }
   }
   public Cart getCartByUsername(String username) {
       Account account = accountRepository.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy tài khoản!"));
       Customer customer = account.getCustomer();
       return cartRepository.findByCustomer(customer).orElse(null);
   }
    @Transactional
    public void removeCartItem(String username, Long variantId) {
        Cart cart = getCartByUsername(username);

        if (cart != null && cart.getCartItem() != null) {
            cart.getCartItem().removeIf(item -> item.getVariants().getId().equals(variantId));

            cartRepository.save(cart);
        }
    }

        @Transactional
        public void clearCart(String username) {

            Cart cart = getCartByUsername(username);

            if (cart != null && cart.getCartItem() != null) {
                cart.getCartItem().clear();

                cartRepository.save(cart);
            }
        }
    @Transactional
    public void updateQuantity(String username, Long variantId, int quantity) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Số lượng không hợp lệ!");
        }

        Cart cart = getCartByUsername(username);
        if (cart == null) {
            throw new Exception("Không tìm thấy giỏ hàng!");
        }

        boolean itemFound = false;

        for (CartItem item : cart.getCartItem()) {
            if (item.getVariants().getId().equals(variantId)) {


                 if (quantity > item.getVariants().getStock()) {
                   throw new Exception("Số lượng vượt quá tồn kho!");
                }

                item.setQuantity(quantity);
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            throw new Exception("Sản phẩm không có trong giỏ hàng!");
        }

        cartRepository.save(cart);
    }
}
