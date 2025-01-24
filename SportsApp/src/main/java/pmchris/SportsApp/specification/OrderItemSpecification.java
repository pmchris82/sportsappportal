package pmchris.SportsApp.specification;


import org.springframework.data.jpa.domain.Specification;
import pmchris.SportsApp.entity.OrderItem;
import pmchris.SportsApp.enums.OrderStatus;
import pmchris.SportsApp.repository.OrderItemRepo;

import java.time.LocalDateTime;

public class OrderItemSpecification {

    /* Specificatin to filer order items by status */
    public static Specification<OrderItem> hasStatus(OrderStatus status) {
        return ((root,query,criteriaBuilder)->status !=null ? criteriaBuilder.equal(root.get("status"), status): null);
    }

    /* Specification to filter order items by data range */
    public static Specification<OrderItem> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ((root, query, criteriaBuilder) -> {
            if (startDate!=null && endDate!=null) {
                return criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
            } else if (startDate!=null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdat"), startDate);
            } else if (endDate!=null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdat"), endDate);
            } else {
                return null;
            }
        });
    }

    /** Specification to filter orderItems by item Id*/
    public static Specification<OrderItem> hasItemId(Long itemId) {
        return ((root,query,criteriaBuilder)->
                itemId != null ? criteriaBuilder.equal(root.get("id"), itemId) : null);
    }
}
