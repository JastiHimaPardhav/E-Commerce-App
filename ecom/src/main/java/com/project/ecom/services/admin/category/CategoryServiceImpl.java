package com.project.ecom.services.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ecom.dto.CategoryDto;
import com.project.ecom.entity.Category;
import com.project.ecom.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepository categoryRepository;
	

	public Category createCategory(CategoryDto categoryDto){ 
		Category category = new Category();
		category.setName (categoryDto.getName()); 
		category.setDescription (categoryDto.getDescription());
		return categoryRepository.save(category);
	}
	
	public List<Category> getAllCategories(){
		return categoryRepository.findAll();
	}
	
}
