package com.mtm.ojt.commons.resources;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.mtm.ojt.persistence.dao.role.RoleDAO;
import com.mtm.ojt.persistence.dao.user.UserDAO;
import com.mtm.ojt.persistence.entities.Role;
import com.mtm.ojt.persistence.entities.User;

@Component
public class UserFactory extends Factory {

    @Autowired
    UserDAO userDAO;
    
    @Autowired
    RoleDAO roleDAO;
    
    @Autowired
    Faker faker;
    
    @Value("${default.user}")
    String defaultUser;
    
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    
    @PostConstruct
    @Override
    public void generated() {
        if(userDAO.dbGetUsers().size() == 0) {
            defaultDataEntry();
        }
        if(userDAO.dbGetUsers().size() < 20) {
            addFakerUsers();
        }
    }

    @Override
    public void defaultDataEntry() {
        String[] def = defaultUser.split(",");
        User u = new User();
        u.setName(def[0]);
        u.setPassword(bCryptPasswordEncoder.encode(def[1]));
        Set<Role> roles = new HashSet<>();
        roles.add(roleDAO.getRoleById(Integer.parseInt(def[2])));
        u.setRoles(roles);
        userDAO.dbAddUser(u);
    }
    
    private void addFakerUsers() {
        for(int i = 0; i < 20 ;i ++) {
            User u = new User();
            u.setName(faker.name().fullName());
            u.setPassword(bCryptPasswordEncoder.encode("111111"));
            Set<Role> roles = new HashSet<>();
            roles.add(roleDAO.getRoleById(3));
            u.setRoles(roles);
            userDAO.dbAddUser(u);
        }
    }
    
}
