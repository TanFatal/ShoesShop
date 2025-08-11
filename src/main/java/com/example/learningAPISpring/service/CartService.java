package com.example.learningAPISpring.service;

import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.repository.UserDetailRepository;
import com.example.learningAPISpring.entity.*;
import com.example.learningAPISpring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserDetailRepository userRepository;

    public Cart getCartByUser(String username) {
        User user = userRepository.findByEmail(username);
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = Cart.builder().user(user).build();
            return cartRepository.save(cart);
        });
    }

    @Transactional
    public Cart addToCart(String username, UUID productId, int quantity) {
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
    public Cart removeFromCart(String username, UUID productId) {
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

