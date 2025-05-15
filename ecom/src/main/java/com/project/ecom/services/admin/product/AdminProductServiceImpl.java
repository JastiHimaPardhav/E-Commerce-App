package com.project.ecom.services.admin.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ecom.dto.ProductDto;
import com.project.ecom.entity.Category;
import com.project.ecom.entity.Product;
import com.project.ecom.repository.CategoryRepository;
import com.project.ecom.repository.ProductRepository;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	

	public ProductDto addProduct (ProductDto productDto) throws IOException, java.io.IOException {
		Product product = new Product();
		product.setName (productDto.getName());
		product.setDescription (productDto.getDescription());
		product.setPrice (productDto.getPrice());
		product.setImage (productDto.getImg().getBytes());
		Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
		product.setCategory(category);
		return productRepository.save(product).getDto();
	}
	

	public List<ProductDto> getAllProducts(){
		List<Product> products = productRepository.findAll();
		return products.stream().map(Product::getDto).collect(Collectors.toList());
	}
	
	public List<ProductDto> getAllProductsByName(String name){
		List<Product> products = productRepository.findAllByNameContaining(name);
		return products.stream().map(Product::getDto).collect(Collectors.toList());
	}
	

	public boolean deleteProduct(Long id) {
		Optional<Product> optionalProduct = productRepository.findById(id);
		if (optionalProduct.isPresent()) {

			productRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
}
