package com.project.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.ecom.entity.Order;
import com.project.ecom.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long>{
//	Order findByUserAndOrderStatus(Long userId, OrderStatus orderStatus);
	@Query("SELECT o FROM Order o WHERE o.user.userId = :userId AND o.orderStatus = :status")
	Order findByUserIdAndOrderStatus(@Param("userId") Long userId, @Param("status") OrderStatus status);

}
