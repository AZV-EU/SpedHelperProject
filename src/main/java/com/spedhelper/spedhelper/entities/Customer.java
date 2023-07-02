package com.spedhelper.spedhelper.entities;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spedhelper.spedhelper.convert.StringListConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String foreName = "";
    private String lastName = "";
    private String email;
    private @Column(length = 60) String passwordHash;
    private ZonedDateTime registrationDate = ZonedDateTime.now();
    private @JsonIgnore @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true) List<ApiKey> apiKeys;
    private @JsonIgnore @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true) List<Vehicle> vehicles;
    private @Convert(converter = StringListConverter.class) List<String> authorities = List.of("USER");
    private Boolean active = true;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }
    @Override
    @JsonIgnore
    public String getPassword() {
        return this.passwordHash;
    }
    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email;
    }
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.active;
    }
}
