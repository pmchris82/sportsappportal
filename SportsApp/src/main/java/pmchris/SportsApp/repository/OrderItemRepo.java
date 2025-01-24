package pmchris.SportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pmchris.SportsApp.entity.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {
}
