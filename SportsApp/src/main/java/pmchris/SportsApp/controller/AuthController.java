package pmchris.SportsApp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pmchris.SportsApp.dto.LoginRequest;
import pmchris.SportsApp.dto.Response;
import pmchris.SportsApp.dto.UserDto;
import pmchris.SportsApp.service.interf.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserDto registrationRequest) {
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest registrationRequest) {
        return ResponseEntity.ok(userService.loginUser(registrationRequest));
    }
}
