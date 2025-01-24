package pmchris.SportsApp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pmchris.SportsApp.dto.AddressDto;
import pmchris.SportsApp.dto.Response;
import pmchris.SportsApp.entity.Address;
import pmchris.SportsApp.entity.User;
import pmchris.SportsApp.repository.AddressRepo;
import pmchris.SportsApp.service.interf.AddressService;
import pmchris.SportsApp.service.interf.UserService;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final UserService userService;

    @Override
    public Response saveAndUpdateAddress(AddressDto addressDto) {
        User user = userService.getLoginUser();
        Address address = user.getAddress();

        if (address == null) {
            address = new Address();
            address.setUser(user);
        }
        if (addressDto.getStreet() != null) address.setStreet(addressDto.getStreet());
        if (addressDto.getCity() != null) address.setCity(addressDto.getCity());
        if (addressDto.getState() != null) address.setState(addressDto.getState());
        if (addressDto.getZipcode() != null) address.setZipcode(addressDto.getZipcode());
        if (addressDto.getCountry() != null) address.setCountry(addressDto.getCountry());
        addressRepo.save(address);
        String message = (user.getAddress()==null)?"Address successfully created":"Address successfully updated";
        return Response.builder()
                .status(200)
                .message(message)
                .build();
    }

}
