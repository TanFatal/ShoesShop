package com.example.learningAPISpring.controllers;

import com.example.learningAPISpring.entity.Cart;
import com.example.learningAPISpring.entity.CartItem;
import com.example.learningAPISpring.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/cart")
@CrossOrigin
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(Principal principal) {
        List<CartItem> items = cartService.getCartItems(principal.getName());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(Principal principal, @RequestParam UUID productId, @RequestParam int quantity) {
        Cart cart = cartService.addToCart(principal.getName(), productId, quantity);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(Principal principal, @RequestParam UUID productId) {
        Cart cart = cartService.removeFromCart(principal.getName(), productId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

