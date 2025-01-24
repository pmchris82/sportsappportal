package pmchris.SportsApp.service.interf;

import pmchris.SportsApp.dto.CategoryDto;
import pmchris.SportsApp.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDto categoryRequest);

    Response updateCategory(Long categoryId, CategoryDto categoryRequest);

    Response getAllCategories();

    Response getCategoryById(Long categoryId);

    Response deleteCategory(Long categoryId);
}

