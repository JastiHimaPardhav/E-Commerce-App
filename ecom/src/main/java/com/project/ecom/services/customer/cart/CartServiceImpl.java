package com.project.ecom.services.customer.cart;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.ecom.dto.AddProductInCartDto;
import com.project.ecom.entity.CartItems;
import com.project.ecom.entity.Order;
import com.project.ecom.entity.Product;
import com.project.ecom.entity.User;
import com.project.ecom.enums.OrderStatus;
import com.project.ecom.repository.CartItemsRepository;
import com.project.ecom.repository.OrderRepository;
import com.project.ecom.repository.ProductRepository;
import com.project.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartItemsRepository cartItemsRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
//	public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
//		Order activeOrder = getOrCreateActiveOrder(addProductInCartDto.getUserId());
//		Optional<CartItems> optionalCartItems = cartItemsRepository.findByProduct_IdAndOrder_IdAndUser_UserId(
//				addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());
//
//		if (optionalCartItems.isPresent()) {
//			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
//		} else {
//			Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
//			Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());
//
//			if (optionalProduct.isPresent() && optionalUser.isPresent()) {
//				CartItems cart = new CartItems();
//				cart.setProduct(optionalProduct.get());
//				cart.setPrice(optionalProduct.get().getPrice());
//				cart.setQuantity(1L);
//				cart.setUser(optionalUser.get());
//				cart.setOrder(activeOrder);
//
//				CartItems updatedCart = cartItemsRepository.save(cart);
//
//				activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
//				activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
//				activeOrder.getCartItems().add(cart);
//
//				orderRepository.save(activeOrder);
//
//				return ResponseEntity.status(HttpStatus.CREATED).body(cart);
//			} else {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
//			}
//		}
//
//	}
//	
//	private Order getOrCreateActiveOrder(Long userId) {
//	    Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
//
//	    if (activeOrder == null) {
//	        Optional<User> optionalUser = userRepository.findById(userId);
//	        if (optionalUser.isEmpty()) {
//	            throw new RuntimeException("User not found"); // or custom exception
//	        }
//
//	        activeOrder = new Order();
//	        activeOrder.setUser(optionalUser.get());
//	        activeOrder.setOrderStatus(OrderStatus.Pending);
//	        activeOrder.setDate(new Date());
//	        activeOrder.setAmount(0L);
//	        activeOrder.setTotalAmount(0L);
//	        activeOrder.setDiscount(0L);
//	        activeOrder.setTrackingId(UUID.randomUUID());
//	        activeOrder.setCartItems(new ArrayList<>());
//
//	        activeOrder = orderRepository.save(activeOrder);
//	    }
//
//	    return activeOrder;
//	}
	
	public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {

	    // 1. Get or create the active order
	    Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
	    if (activeOrder == null) {
	        Optional<User> userOpt = userRepository.findById(addProductInCartDto.getUserId());
	        if (userOpt.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }

	        activeOrder = new Order();
	        activeOrder.setUser(userOpt.get());
	        activeOrder.setOrderStatus(OrderStatus.Pending);
	        activeOrder.setAmount(0L);
	        activeOrder.setTotalAmount(0L);
	        activeOrder.setDate(new Date());
	        activeOrder.setTrackingId(UUID.randomUUID());

	        activeOrder = orderRepository.save(activeOrder);
	    }

	    // 2. Check if product already exists in cart
	    Optional<CartItems> optionalCartItems = cartItemsRepository.findByProduct_IdAndOrder_IdAndUser_UserId(
	        addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

	    if (optionalCartItems.isPresent()) {
	        CartItems existingCart = optionalCartItems.get();
	        existingCart.setQuantity(existingCart.getQuantity() + 1);
	        existingCart.setPrice(existingCart.getProduct().getPrice() * existingCart.getQuantity());
	        cartItemsRepository.save(existingCart);

	        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + existingCart.getProduct().getPrice());
	        activeOrder.setAmount(activeOrder.getAmount() + existingCart.getProduct().getPrice());
	        orderRepository.save(activeOrder);

	        return ResponseEntity.status(HttpStatus.OK).body(existingCart);
	    } else {
	        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
	        Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());

	        if (optionalProduct.isPresent() && optionalUser.isPresent()) {
	            CartItems cart = new CartItems();
	            cart.setProduct(optionalProduct.get());
	            cart.setPrice(optionalProduct.get().getPrice());
	            cart.setQuantity(1L);
	            cart.setUser(optionalUser.get());
	            cart.setOrder(activeOrder);

	            CartItems updatedCart = cartItemsRepository.save(cart);

	            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
	            activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
	            orderRepository.save(activeOrder);

	            return ResponseEntity.status(HttpStatus.CREATED).body(updatedCart);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
	        }
	    }
	}


	
}
