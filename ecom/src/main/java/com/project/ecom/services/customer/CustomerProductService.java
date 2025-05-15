package com.project.ecom.services.customer;

import java.util.List;

import com.project.ecom.dto.ProductDto;

public interface CustomerProductService {
	
	List<ProductDto> getAllProducts();
	
	List<ProductDto> getAllProductsByName(String name);

}
