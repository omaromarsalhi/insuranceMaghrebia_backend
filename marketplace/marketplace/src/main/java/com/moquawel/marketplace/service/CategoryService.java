package com.moquawel.marketplace.service;

import com.moquawel.marketplace.category.Category;
import com.moquawel.marketplace.category.CategoryRepository;
import com.moquawel.marketplace.mkservice.MyServiceRepository;
import com.moquawel.marketplace.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {


    private final CategoryRepository categoryRepository;
    private final MyServiceRepository myServiceRepository;


    public void saveCategory(String name) {
        try {
            categoryRepository.save(Category
                    .builder()
                    .name(name)
                    .numberOfServices(0)
                    .build()
            );
        } catch (Exception e) {
            log.error("this err occurred while saving the new category: {}", e.getMessage());
        }
    }


    public ApiResponse deleteCategory(String categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            myServiceRepository.deleteMyServiceByCategoryId(categoryId);
            return new ApiResponse("category and services are deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("this err occurred while deleting category with id: {} : {}", categoryId, e.getMessage());
            return new ApiResponse("Entity with id " + categoryId + " did not get deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
