package pmchris.SportsApp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pmchris.SportsApp.entity.User;
import pmchris.SportsApp.exception.NotFoundException;
import pmchris.SportsApp.repository.UserRepo;

@Service
@RequiredArgsConstructor

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByemail(username)
                .orElseThrow(() -> new NotFoundException("User/email not found"));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}
