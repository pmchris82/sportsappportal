package pmchris.SportsApp.service.interf;

import pmchris.SportsApp.dto.LoginRequest;
import pmchris.SportsApp.dto.Response;
import pmchris.SportsApp.dto.UserDto;
import pmchris.SportsApp.entity.User;

public interface UserService {

    Response registerUser(UserDto registrationRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getLoginUser();
    Response getUserInfoAndOrderHistory();
}
