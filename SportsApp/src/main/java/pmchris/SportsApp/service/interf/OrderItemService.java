package pmchris.SportsApp.service.interf;

import org.springframework.data.domain.Pageable;
import pmchris.SportsApp.dto.OrderRequest;
import pmchris.SportsApp.dto.Response;
import pmchris.SportsApp.enums.OrderStatus;


import java.time.LocalDateTime;

public interface OrderItemService {
    Response placeOrder(OrderRequest orderRequest);

    Response updateOrderItemStatus(Long orderItemId, String status);

    Response filterOrderItems (OrderStatus status, LocalDateTime starDate, LocalDateTime endDate, Long itemId, Pageable pageable);
}
