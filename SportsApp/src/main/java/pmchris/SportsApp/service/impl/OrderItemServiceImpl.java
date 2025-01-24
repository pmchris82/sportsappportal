package pmchris.SportsApp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pmchris.SportsApp.dto.OrderItemDto;
import pmchris.SportsApp.dto.OrderRequest;
import pmchris.SportsApp.dto.Response;
import pmchris.SportsApp.entity.Order;
import pmchris.SportsApp.entity.OrderItem;
import pmchris.SportsApp.entity.Product;
import pmchris.SportsApp.entity.User;
import pmchris.SportsApp.enums.OrderStatus;
import pmchris.SportsApp.exception.NotFoundException;
import pmchris.SportsApp.mapper.EntityDtoMapper;
import pmchris.SportsApp.repository.OrderItemRepo;
import pmchris.SportsApp.repository.OrderRepo;
import pmchris.SportsApp.repository.ProductRepo;
import pmchris.SportsApp.service.interf.OrderItemService;
import pmchris.SportsApp.service.interf.ProductService;
import pmchris.SportsApp.service.interf.UserService;
import pmchris.SportsApp.specification.OrderItemSpecification;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j

public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final EntityDtoMapper entityDtoMapper;
    private final UserService userService;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {

        User user = userService.getLoginUser();
        //mapOrderRequest items to order entities
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(orderItemRequest -> {
                Product product = productRepo.findById(orderItemRequest.getProductId())
                        .orElseThrow(() -> new NotFoundException("Product not found"));
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(orderItemRequest.getQuantity());
                orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.getQuantity()))); //Setting price based on quantity
                orderItem.setStatus(OrderStatus.PENDING);
                orderItem.setUser(user);
                return orderItem;
        }).toList();

        //Calculate Total Price
        BigDecimal totalPrice = orderRequest.getTotalPrice() != null && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) > 0
                ? orderRequest.getTotalPrice()
                : orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order entity

        Order order = new Order();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);

        //set the order reference in each order item
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        orderRepo.save(order);

        return Response.builder()
                .status(200)
                .message("Order was successfully placed")
                .build();
    }

    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException("Order item not found"));

        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepo.save(orderItem);
        return Response.builder()
                .status(200)
                .message("Status updated Successfully")
                .build();
    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime starDate, LocalDateTime endDate, Long itemId, Pageable pageable) {
        Specification<OrderItem> spec = Specification.where(OrderItemSpecification.hasStatus(status))
                .and(OrderItemSpecification.createdBetween(starDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));
        Page<OrderItem> orderItemPage = orderItemRepo.findAll(spec, pageable);

        if(orderItemPage.isEmpty()){
            throw new NotFoundException("Order not found");
        }
        List<OrderItemDto> orderItemDtos = orderItemPage.getContent().stream()
                .map(entityDtoMapper::mapOrderItemDtoPlusProductAndUser)
                .toList();

        return Response.builder()
                .status(200)
                .orderItemList(orderItemDtos)
                .totalPage(orderItemPage.getTotalPages())
                .totalElement(orderItemPage.getTotalElements())
                .build();
    }
}
