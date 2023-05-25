package com.mtm.ojt.api.request;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.mtm.ojt.web.forms.UserForm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank
    String name;

    List<Integer> roles;
    
    public UserUpdateRequest(UserForm form) {
        this.name = form.getName();
        this.roles = form.getRoles();
    }
}
