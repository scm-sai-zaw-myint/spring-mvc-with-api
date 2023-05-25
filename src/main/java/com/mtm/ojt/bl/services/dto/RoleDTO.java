package com.mtm.ojt.bl.services.dto;

import com.mtm.ojt.persistence.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    
    Integer id;
    String name;
    
    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

}
