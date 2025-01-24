package pmchris.SportsApp.service.interf;

import pmchris.SportsApp.dto.AddressDto;
import pmchris.SportsApp.dto.Response;

public interface AddressService {
    Response saveAndUpdateAddress(AddressDto addressDto);
}
