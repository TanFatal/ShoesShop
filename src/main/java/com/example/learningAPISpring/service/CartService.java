package com.example.learningAPISpring.service;

import com.example.learningAPISpring.entity.*;
import com.example.learningAPISpring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public Cart getCartByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = Cart.builder().user(user).build();
            return cartRepository.save(cart);
        });
    }

    @Transactional
    public Cart addToCart(String username, Long productId, int quantity) {
        Cart cart = getCartByUser(username);
        Product product = productRepository.findById(productId).orElseThrow();
        Optional<CartItem> existing = cartItemRepository.findByCartAndProduct(cart, product);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem item = CartItem.builder().cart(cart).product(product).quantity(quantity).build();
            cartItemRepository.save(item);
        }
        return getCartByUser(username);
    }

    @Transactional
    public Cart removeFromCart(String username, Long productId) {
        Cart cart = getCartByUser(username);
        Product product = productRepository.findById(productId).orElseThrow();
        cartItemRepository.findByCartAndProduct(cart, product).ifPresent(cartItemRepository::delete);
        return getCartByUser(username);
    }

    @Transactional
    public void clearCart(String username) {
        Cart cart = getCartByUser(username);
        List<CartItem> items = cartItemRepository.findByCart(cart);
        cartItemRepository.deleteAll(items);
    }

    public List<CartItem> getCartItems(String username) {
        Cart cart = getCartByUser(username);
        return cartItemRepository.findByCart(cart);
    }
}

