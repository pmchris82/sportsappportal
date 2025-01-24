package pmchris.SportsApp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pmchris.SportsApp.dto.CategoryDto;
import pmchris.SportsApp.dto.Response;
import pmchris.SportsApp.entity.Category;
import pmchris.SportsApp.exception.NotFoundException;
import pmchris.SportsApp.mapper.EntityDtoMapper;
import pmchris.SportsApp.repository.CategoryRepo;
import pmchris.SportsApp.service.interf.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response createCategory(CategoryDto categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        categoryRepo.save(category);
        return Response.builder()
                .status(200)
                .message("Category created successfully")
                .build();
    }

    @Override
    public Response updateCategory(Long categoryId, CategoryDto categoryRequest) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(categoryRequest.getName());
        categoryRepo.save(category);
        return Response.builder()
                .status(200)
                .message("Category updtedd sucessfully")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDto> categoryDtoList = categories.stream()
                .map(entityDtoMapper::mapCategoryToDtoBasic)
                .collect(Collectors.toList());
        return Response.builder()
                .status(200)
                .categoryList(categoryDtoList)
                .build();
    }

    @Override
    public Response getCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        CategoryDto categoryDto = entityDtoMapper.mapCategoryToDtoBasic(category);
        return Response.builder()
                .status(200)
                .category(categoryDto)
                .build();
    }

    @Override
    public Response deleteCategory(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepo.delete(category);
        return Response.builder()
                .status(200)
                .message("Category deleted")
                .build();
    }
}
