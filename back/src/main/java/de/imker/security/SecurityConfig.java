package de.imker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.imker.dto.StandardResponseDto;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Autowired
  private ObjectMapper objectMapper;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable();
    httpSecurity.headers().frameOptions().disable();

    httpSecurity
        .authorizeRequests()
        .antMatchers("/swagger-ui/**").permitAll()
        .antMatchers("/api/login/**").permitAll()
        .antMatchers("/api/logout/**").permitAll()
        .antMatchers("/api/restore/**").permitAll()
        .antMatchers(HttpMethod.POST, "/api/register/**").permitAll()
        .antMatchers("/api/users/**").authenticated()
        .and()
        .formLogin()
        .loginProcessingUrl("/api/login")
        .successHandler((request, response, authentication) -> {
          fillResponse(response, HttpStatus.OK, "Login successful");
        })
        .failureHandler(((request, response, exception) -> {
          fillResponse(response, HttpStatus.UNAUTHORIZED, "Incorrect username or password");
        }))
        .and()
        .exceptionHandling()
        .defaultAuthenticationEntryPointFor(((request, response, authException) -> {
          fillResponse(response, HttpStatus.UNAUTHORIZED, "User unauthorized");
        }), new AntPathRequestMatcher("/api/**"))
        .accessDeniedHandler((request, response, accessDeniedException) -> {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

          fillResponse(response, HttpStatus.FORBIDDEN, "Access denied for user with email <" +
              authentication.getName() + "> and role " + authentication.getAuthorities());

        })
        .and()
        .logout()
        .logoutUrl("/api/logout")
        .logoutSuccessHandler((request, response, authentication) -> {
          fillResponse(response, HttpStatus.OK, "Logout successful");
        });
    return httpSecurity.build();
  }

  private void fillResponse(HttpServletResponse response, HttpStatus status, String message) {
    try {
      response.setStatus(status.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);

      StandardResponseDto responseDto = StandardResponseDto.builder()
          .message(message)
          .build();

      String body = objectMapper.writeValueAsString(responseDto);

      response.getWriter().write(body);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Autowired
  public void bindUserDetailsServiceAndPasswordEncoder(
      PasswordEncoder passwordEncoder,
      UserDetailsService authenticatedUsersService,
      AuthenticationManagerBuilder authenticationManager) throws Exception {
    authenticationManager.userDetailsService(authenticatedUsersService).passwordEncoder(passwordEncoder);
  }
}
