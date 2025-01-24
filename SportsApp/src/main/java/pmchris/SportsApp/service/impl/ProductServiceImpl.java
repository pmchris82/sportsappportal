package pmchris.SportsApp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pmchris.SportsApp.dto.ProductDto;
import pmchris.SportsApp.dto.Response;
import pmchris.SportsApp.entity.Category;
import pmchris.SportsApp.entity.Product;
import pmchris.SportsApp.exception.NotFoundException;
import pmchris.SportsApp.mapper.EntityDtoMapper;
import pmchris.SportsApp.repository.CategoryRepo;
import pmchris.SportsApp.repository.ProductRepo;
import pmchris.SportsApp.service.AwsS3Service;
import pmchris.SportsApp.service.interf.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final EntityDtoMapper entityDtoMapper;
    private final AwsS3Service awsS3Service;

    @Override
    public Response createProduct(Long categoryId, String name, BigDecimal price, String description, MultipartFile image) {

    Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        String productImageUrl = awsS3Service.saveImageToS3(image);

        Product product = new Product();
        product.setCategory(category);
        product.setPrice(price);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(productImageUrl);

        productRepo.save(product);

        return Response.builder()
                .status(200)
                .message("Product successfully created")
                .build();
    }

    @Override
    public Response updateProduct(Long productId, Long categoryId, String name, BigDecimal price, String description, MultipartFile image) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));

        Category category = null;
        String productImageUrl = null;


        if (category != null) {
            category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        }
        if (image != null) {
            productImageUrl = awsS3Service.saveImageToS3(image);
        }

        if (category != null) product.setCategory(category);
        if (name != null) product.setName(name);
        if (price != null) product.setPrice(price);
        if (description != null) product.setDescription(description);
        if (productImageUrl != null) product.setImageUrl(productImageUrl);

        productRepo.save(product);

        return Response.builder()
                .status(200)
                .message("Product updated suceesfully")
                .build();
    }

    @Override
    public Response deleteProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        productRepo.delete(product);

        return Response.builder()
                .status(200)
                .message(" Product deleted successfully")
                .build();
    }

    @Override
    public Response getProductById(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
        ProductDto productDto = entityDtoMapper.mapProductToDtoBasic(product);

        return Response.builder()
                .status(200)
                .product(productDto)
                .build();
    }

    @Override
    public Response getAllProducts() {

        List<ProductDto> productList = productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream().
                map(entityDtoMapper::mapProductToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .productList(productList)
                .build();
    }

    @Override
    public Response getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepo.findByCategoryId(categoryId);
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));

        if (products.isEmpty()){
            throw new NotFoundException("No product found for category" + category.getName());
        }
        List<ProductDto>productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }

    @Override
    public Response searchProducts(String searchValue) {
        List<Product> products = productRepo.findByNameContainingOrDescriptionContaining(searchValue, searchValue);
        if (products.isEmpty()){
            throw new NotFoundException("No product found for " + searchValue);
        }
        List<ProductDto> productDtoList = products.stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .productList(productDtoList)
                .build();
    }
}
