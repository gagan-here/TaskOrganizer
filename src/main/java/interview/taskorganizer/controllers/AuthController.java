package interview.taskorganizer.controllers;

import interview.taskorganizer.dto.LoginDto;
import interview.taskorganizer.dto.LoginResponseDto;
import interview.taskorganizer.dto.RegisterDto;
import interview.taskorganizer.dto.UserDto;
import interview.taskorganizer.services.authentication.AuthService;
import interview.taskorganizer.services.authentication.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<UserDto> register(@RequestBody RegisterDto registerDto) {
    UserDto userDto = userService.registerUser(registerDto);
    return ResponseEntity.ok(userDto);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
    LoginResponseDto loginResponseDto = authService.login(loginDto);
    return ResponseEntity.ok(loginResponseDto);
  }
}
