package pmchris.SportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pmchris.SportsApp.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
