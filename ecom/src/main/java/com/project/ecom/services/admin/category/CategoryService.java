package com.project.ecom.services.admin.category;

import java.util.List;

import com.project.ecom.dto.CategoryDto;
import com.project.ecom.entity.Category;

public interface CategoryService {
	
	Category createCategory (CategoryDto categoryDto);
	
	List<Category> getAllCategories();

}
