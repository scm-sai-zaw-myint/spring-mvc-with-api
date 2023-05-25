package com.mtm.ojt.commons.resources;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mtm.ojt.persistence.dao.role.RoleDAO;
import com.mtm.ojt.persistence.entities.Role;

@Component
public class RolesFactory extends Factory {

    @Value("${default.roles}")
    String roles;
    
    @Autowired
    RoleDAO roleDAO;
    
    @PostConstruct
    @Override
    public void generated() {
        if(roleDAO.getRole().size() == 0) {
            defaultDataEntry();
        }
    }
    
    @Override
    public void defaultDataEntry() {
        List<String> rolesList = Arrays.asList(roles.split(","));
        for(String role:rolesList) {
            this.roleDAO.createRole(new Role(role));
        }
    }
}
