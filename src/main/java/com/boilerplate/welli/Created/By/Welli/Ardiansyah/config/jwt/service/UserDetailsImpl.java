package com.boilerplate.welli.Created.By.Welli.Ardiansyah.config.jwt.service;

import com.boilerplate.welli.Created.By.Welli.Ardiansyah.domain.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String fullname;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Collection<? extends GrantedAuthority> permissions;

    public UserDetailsImpl(UUID id, String email, String fullname, String password,
                           Collection<? extends GrantedAuthority> authorities,
                           Collection<? extends GrantedAuthority> permissions) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.permissions = permissions;
    }

    public static UserDetailsImpl build(UserEntity user, Collection<? extends GrantedAuthority> permissions) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())),
                permissions.stream()
        ).collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getFullname(),
                user.getPassword(),
                authorities,
                permissions
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Collection<? extends GrantedAuthority> getPermissions() {
        return permissions;
    }

    public UUID getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }
}
