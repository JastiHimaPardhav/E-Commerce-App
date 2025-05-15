package com.project.ecom.services.admin.product;

import java.util.List;

import com.project.ecom.dto.ProductDto;

import io.jsonwebtoken.io.IOException;

public interface AdminProductService {
	
	ProductDto addProduct (ProductDto productDto) throws IOException, java.io.IOException;

	List<ProductDto> getAllProducts();
	
	List<ProductDto> getAllProductsByName(String name);
	
	boolean deleteProduct(Long id);
	
}
