package pmchris.SportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pmchris.SportsApp.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
