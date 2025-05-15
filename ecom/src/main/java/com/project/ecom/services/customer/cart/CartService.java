package com.project.ecom.services.customer.cart;

import org.springframework.http.ResponseEntity;

import com.project.ecom.dto.AddProductInCartDto;

public interface CartService {
	ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto);
}
