package pmchris.SportsApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pmchris.SportsApp.entity.Address;

public interface AddressRepo extends JpaRepository<Address, Long> {
}
