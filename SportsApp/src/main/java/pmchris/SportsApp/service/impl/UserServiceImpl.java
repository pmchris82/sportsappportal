package pmchris.SportsApp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pmchris.SportsApp.dto.LoginRequest;
import pmchris.SportsApp.dto.Response;
import pmchris.SportsApp.dto.UserDto;
import pmchris.SportsApp.entity.User;
import pmchris.SportsApp.enums.UserRole;
import pmchris.SportsApp.exception.InvalidCredentialException;
import pmchris.SportsApp.exception.NotFoundException;
import pmchris.SportsApp.mapper.EntityDtoMapper;
import pmchris.SportsApp.repository.UserRepo;
import pmchris.SportsApp.security.JwtUtils;
import pmchris.SportsApp.service.interf.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Response registerUser(UserDto registrationRequest) {
        UserRole role =UserRole.USER;
        if ((registrationRequest.getRole() != null) && registrationRequest.getRole().name().equalsIgnoreCase("admin")){
            role = UserRole.ADMIN;
        }
        User user = User.builder()
                .email(registrationRequest.getEmail())
                .names(registrationRequest.getNames())
                .lastNames(registrationRequest.getLastNames())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .build();
        User savedUser = userRepo.save(user);

        UserDto userDto = entityDtoMapper.mapUserToDtoBasic(savedUser);

        return Response.builder()
                .status(200)
                .message("User successfully created")
                .user(userDto)
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {

        User user = userRepo.findByemail(loginRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialException("Password does not match");
        }
        String token = jwtUtils.generateToken(user);

        return Response.builder()
                .status(200)
                .message("User successfully logged in")
                .token(token)
                .expirationTime("6 months")
                .role(user.getRole().name())
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream()
                .map(entityDtoMapper::mapUserToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        log.info("User / email is: {}", email);
        return userRepo.findByemail(email)
                .orElseThrow(() -> new UsernameNotFoundException(("User not found")));
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);

        return Response.builder()
                .status(200)
                .user(userDto)
                .build();
    }
}
