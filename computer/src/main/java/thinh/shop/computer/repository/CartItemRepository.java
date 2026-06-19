package thinh.shop.computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thinh.shop.computer.entity.Cart;
import thinh.shop.computer.entity.CartItem;
import thinh.shop.computer.entity.ProductVariant;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByCartAndVariants(Cart cart, ProductVariant productVariant);

    List<CartItem> getCartItemsByCart(Cart cart);

    void deleteByCartAndVariants(Cart cart, ProductVariant variants);
}
