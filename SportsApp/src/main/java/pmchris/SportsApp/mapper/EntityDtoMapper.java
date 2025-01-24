package pmchris.SportsApp.mapper;

import org.springframework.stereotype.Component;
import pmchris.SportsApp.dto.*;
import pmchris.SportsApp.entity.*;

import java.util.stream.Collectors;

@Component
public class EntityDtoMapper {

    //User Entity to UserDto Basic
    public UserDto mapUserToDtoBasic(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());

        userDto.setEmail(user.getEmail());
        userDto.setNames(user.getNames());
        userDto.setLastNames(user.getLastNames());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        return userDto;
    }

    //Address to Dto Basic
    public AddressDto mapAddressToDtoBasic(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setId(address.getId());
        addressDto.setCity(address.getCity());
        addressDto.setStreet(address.getStreet());
        addressDto.setState(address.getState());
        addressDto.setCountry(address.getCountry());
        addressDto.setZipcode(address.getZipcode());
        return addressDto;
    }

    //Map Category to DTO Basic
    public CategoryDto mapCategoryToDtoBasic(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    //OrderItem to DTO Basics
    public OrderItemDto mapOrderItemToDtoBasic(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setStatus(orderItem.getStatus());
        orderItemDto.setCreatedAt(orderItem.getCreatedAt());
        return orderItemDto;
    }

    //Product ro DTO Basic
    public ProductDto mapProductToDtoBasic(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setImageUrl(product.getImageUrl());
        return productDto;
    }

    //User to DTO plus Address
    public UserDto mapUserDtoPlusAddress(User user){
        UserDto userDto = mapUserToDtoBasic(user);
        if (user.getAddress() != null) {
            AddressDto addressDto = mapAddressToDtoBasic(user.getAddress());
            userDto.setAddress(addressDto);
        }
        return userDto;
    }

    //OrderItem to DTO plus product
    public OrderItemDto mapOrderItemDtoPlusProduct(OrderItem orderItem){
        OrderItemDto orderItemDto = mapOrderItemToDtoBasic(orderItem);
        if (orderItem.getProduct() != null) {
            ProductDto productDto = mapProductToDtoBasic(orderItem.getProduct());
            orderItemDto.setProduct(productDto);
        }
        return orderItemDto;
    }

    //OrderItem to DTO plus product and user
    public OrderItemDto mapOrderItemDtoPlusProductAndUser(OrderItem orderItem){
        OrderItemDto orderItemDto = mapOrderItemDtoPlusProduct(orderItem);
        if (orderItem.getUser() != null) {
            UserDto userDto = mapUserDtoPlusAddress(orderItem.getUser());
            orderItemDto.setUser(userDto);
        }
        return orderItemDto;
    }

    //User to DTO with Address and Order Items History
    public UserDto mapUserToDtoPlusAddressAndOrderHistory(User user){
        UserDto userDto = mapUserDtoPlusAddress(user);
        if (user.getOrderItemList() != null && !user.getOrderItemList().isEmpty()) {
            userDto.setOrderItemList(user.getOrderItemList()
                    .stream()
                    .map(this::mapOrderItemDtoPlusProduct)
                    .collect(Collectors.toList()));
        }
        return userDto;
    }
}
