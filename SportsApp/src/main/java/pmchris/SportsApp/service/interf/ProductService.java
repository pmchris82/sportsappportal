package pmchris.SportsApp.service.interf;

import org.springframework.web.multipart.MultipartFile;
import pmchris.SportsApp.dto.Response;

import java.math.BigDecimal;

public interface ProductService {

    Response createProduct(Long categoryId, String name, BigDecimal price, String description, MultipartFile image);

    Response updateProduct(Long productId, Long categoryId, String name, BigDecimal price, String description, MultipartFile image);

    Response deleteProduct(Long productId);

    Response getProductById(Long productId);

    Response getAllProducts ();

    Response getProductsByCategoryId(Long categoryId);

    Response searchProducts(String searchValue);



}
