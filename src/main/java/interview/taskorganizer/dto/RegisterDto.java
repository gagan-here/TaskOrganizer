package interview.taskorganizer.dto;

import interview.taskorganizer.enums.Roles;
import java.util.Set;
import lombok.Data;

@Data
public class RegisterDto {
  private String email;
  private String password;
  private String name;
  private Set<Roles> roles;
}
