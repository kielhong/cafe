package com.widehouse.cafe.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties({"password", "enabled", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @NotNull
    @Column(nullable = false, length = 80)
    private String password;

    private String nickname;

    @Email
    private String email;

    public Member(@Size(max = 30) String username, String password, String nickname, @Email String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
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
}
