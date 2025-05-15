package com.project.ecom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecom.entity.CartItems;

@Repository
public interface CartItemsRepository extends JpaRepository <CartItems, Long> {
	Optional<CartItems> findByProduct_IdAndOrder_IdAndUser_UserId(Long productId, Long orderId, Long userId);
}
