package com.project.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecom.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Long>{

}
