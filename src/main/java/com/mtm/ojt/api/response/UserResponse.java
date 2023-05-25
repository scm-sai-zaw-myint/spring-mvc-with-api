package com.mtm.ojt.api.response;

import java.util.List;

import com.mtm.ojt.api.commons.reponse.APIResponse;
import com.mtm.ojt.bl.services.dto.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends APIResponse {

    public UserResponse(Integer code, String message, Long timestamp, String name, List<String> roles) {
        super(code, message, timestamp);
        this.name = name;
        this.roles = roles;
    }

    String name;
    List<String> roles;

    public UserResponse(Integer code, String message, Long timestamp, UserDTO dto) {
        super(code, message, timestamp);
        this.name = dto.getName();
        this.roles = dto.getRoles().stream().map(r -> r.getName()).toList();
    }
    
    public UserResponse(String name, List<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public UserResponse(UserDTO dto) {
        this.name = dto.getName();
        this.roles = dto.getRoles().stream().map(r -> r.getName()).toList();
    }
}
