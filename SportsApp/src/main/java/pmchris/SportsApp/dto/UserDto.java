package pmchris.SportsApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pmchris.SportsApp.enums.UserRole;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String names;
    private String lastNames;
    private String password;
    private String phoneNumber;
    private UserRole role;
    private List<OrderItemDto> orderItemList;
    private AddressDto address;
}
