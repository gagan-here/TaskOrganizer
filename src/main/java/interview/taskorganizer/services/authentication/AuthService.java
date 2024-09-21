package interview.taskorganizer.services.authentication;

import interview.taskorganizer.dto.LoginDto;
import interview.taskorganizer.dto.LoginResponseDto;
import interview.taskorganizer.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public LoginResponseDto login(LoginDto loginDto) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
    User user = (User) authentication.getPrincipal();
    String accessToken = jwtService.generateToken(user);
    return new LoginResponseDto(user.getId(), accessToken);
  }
}
