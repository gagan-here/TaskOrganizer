package interview.taskorganizer.config;

import static interview.taskorganizer.enums.Roles.ADMIN;
import static interview.taskorganizer.enums.Roles.USER;

import interview.taskorganizer.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  private static final String[] PUBLIC_ROUTES = {"/auth/**"};
  private static final String[] ADMIN_ONLY_ROUTES = {"/tasks", "/tasks/{id}", "/tasks/{id}"};
  private static final String[] USER_ONLY_ROUTES = {"/tasks/{id}/**"};

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeHttpRequests(
            auth ->
                auth
                    // Endpoints, accessible by everyone
                    .requestMatchers(PUBLIC_ROUTES)
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/tasks/**")
                    .permitAll()

                    // Endpoints accessible to Admin only (POST, PUT, DELETE)
                    .requestMatchers(HttpMethod.POST, ADMIN_ONLY_ROUTES[0])
                    .hasRole(ADMIN.name())
                    .requestMatchers(HttpMethod.PUT, ADMIN_ONLY_ROUTES[1])
                    .hasRole(ADMIN.name())
                    .requestMatchers(HttpMethod.DELETE, ADMIN_ONLY_ROUTES[2])
                    .hasRole(ADMIN.name())

                    // Permitted to both User and Admin
                    .requestMatchers(HttpMethod.PUT, USER_ONLY_ROUTES)
                    .hasAnyRole(USER.name(), ADMIN.name())

                    // Any other request requires authentication
                    .anyRequest()
                    .authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
