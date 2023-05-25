package com.mtm.ojt.web.forms;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.mtm.ojt.api.request.UserUpdateRequest;
import com.mtm.ojt.bl.services.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {

    Integer id;

    @NotBlank
    String name;

    String password;

    @NotEmpty
    List<Integer> roles;

    public UserForm(UserDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.roles = dto.getRoles().stream().map(r -> r.getId()).toList();
    }

    public UserForm(UserUpdateRequest request) {
        this.name = request.getName();
        if(request.getRoles()!=null && request.getRoles().size() > 0) this.roles = request.getRoles();
    }
    
    @Override
    public String toString() {
        return "name = " + this.name + ", roles = " + this.roles;
    }
}
