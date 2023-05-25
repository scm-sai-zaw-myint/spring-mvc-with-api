package com.mtm.ojt.bl.services.dto;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mtm.ojt.persistence.entities.Role;
import com.mtm.ojt.persistence.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements UserDetails{
    
    /**
     * 
     */
    private static final long serialVersionUID = 7412957914918910845L;

    Integer id;
    
    String name;
    
    @JsonIgnore
    String password;
    
    @JsonIgnore
    String tokenIp;
    
    Set<Role> roles;
    
    Date createdAt;
    
    Date updatedAt;
    
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(t -> new SimpleGrantedAuthority(t.getName())).toList();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.name;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserDTO(User user) {
        super();
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.tokenIp = user.getTokenIp();
    }

    public UserDTO(Integer id, String name, String password, Set<Role> roles) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }
    
}
