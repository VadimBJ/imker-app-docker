package de.imker.security.details;

import de.imker.models.User.State;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import de.imker.models.User;

public class AuthenticatedUser implements UserDetails {

  private final User user;

  public AuthenticatedUser(User user) {
    this.user = user;
  }

  public Long id() {
    return user.getId();
  }

  public User getUser() {
    return user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
  }

  @Override
  public String getPassword() {
    return user.getHashPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !user.getState().equals(User.State.DELETED) &
        !user.getState().equals(State.NOT_CONFIRMED) &
        !user.getState().equals(State.BANNED);
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.getState().equals(User.State.CONFIRMED);
  }
}
