package org.ifsul.carhired.api.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.ifsul.carhired.api.user.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {
  ADMIN(Set.of(
      ADMIN_CREATE,
      ADMIN_READ,
      ADMIN_UPDATE,
      ADMIN_DELETE,
      CUSTOMER_CREATE,
      CUSTOMER_READ,
      CUSTOMER_UPDATE,
      CUSTOMER_DELETE)),

  CUSTOMER(Set.of(
      CUSTOMER_CREATE,
      CUSTOMER_READ,
      CUSTOMER_UPDATE,
      CUSTOMER_DELETE));

  private final Set<Permission> permissions;

  public @NonNull List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
        .stream()
        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
        .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
