package interview.taskorganizer.services.authentication;

import interview.taskorganizer.dto.RegisterDto;
import interview.taskorganizer.dto.UserDto;
import interview.taskorganizer.entities.User;
import interview.taskorganizer.exceptions.ResourceNotFoundException;
import interview.taskorganizer.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(username)
        .orElseThrow(
            () -> new BadCredentialsException("User with email " + username + " not found"));
  }

  public User getUserById(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
  }

  public UserDto registerUser(RegisterDto registerDto) {
    Optional<User> user = userRepository.findByEmail(registerDto.getEmail());
    if (user.isPresent()) {
      throw new BadCredentialsException("User with email already exits " + registerDto.getEmail());
    }

    User toBeCreatedUser = modelMapper.map(registerDto, User.class);
    toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

    User savedUser = userRepository.save(toBeCreatedUser);
    return modelMapper.map(savedUser, UserDto.class);
  }
}
