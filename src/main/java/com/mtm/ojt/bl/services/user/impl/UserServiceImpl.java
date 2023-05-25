package com.mtm.ojt.bl.services.user.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mtm.ojt.bl.services.dto.UserDTO;
import com.mtm.ojt.bl.services.user.UserService;
import com.mtm.ojt.persistence.dao.role.RoleDAO;
import com.mtm.ojt.persistence.dao.user.UserDAO;
import com.mtm.ojt.persistence.entities.Role;
import com.mtm.ojt.persistence.entities.User;
import com.mtm.ojt.web.forms.UserForm;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    RoleDAO roleDAO;
    
    @Override
    public List<UserDTO> doGetUserList() {
        List<User> userList = userDAO.dbGetUsers();
        return userList.stream().map(u -> new UserDTO(u)).toList();
    }

    @Override
    public UserDTO doGetUserById(Integer id) {
        return new UserDTO(this.userDAO.dbGetUserById(id));
    }

    @Override
    public UserDTO doAddUser(UserForm form) {
        User user = new User();
        user.setName(form.getName());
        user.setPassword(bCryptPasswordEncoder.encode(form.getPassword()));
        Set<Role> roles = new HashSet<>(form.getRoles().stream().map(r -> roleDAO.getRoleById(r)).toList());
        user.setRoles(roles);
        User u = this.userDAO.dbAddUser(user);
        return new UserDTO(u);
    }

    @Override
    public UserDTO doUpdateUser(UserForm form) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userDAO.dbGetUserById(form.getId());
        user.setName(form.getName());
        UserDetails loggedUser = (UserDetails) authentication.getPrincipal();
        UserDTO dto = doGetUserByName(loggedUser.getUsername());
        
        if(dto.getRoles().stream().map(r -> r.getName()).toList().contains("ROLE_ADMIN") && form.getRoles() != null && form.getRoles().size() > 0) {
            Set<Role> roles = new HashSet<>(form.getRoles().stream().map(r -> roleDAO.getRoleById(r)).toList());
            user.setRoles(roles);
        }
        //current logged User
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User currentLoggedUser = this.userDAO.getUserByName(userDetails.getUsername());
        
        User updatedUser = this.userDAO.dbUpdateUser(user);
        UserDTO updateDto = new UserDTO(updatedUser);
        
        //update principal if logged user is update itself
        udpateAuthentication(currentLoggedUser,updateDto);
        
        return updateDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getUserByName(username);
        if(user == null) {
            throw new UsernameNotFoundException("Bad Credential!");
        }
        return new UserDTO(user);
    }

    @Override
    public UserDTO doGetUserByName(String name) {
        User user = userDAO.getUserByName(name);
        return new UserDTO(user);
    }
    
    @Override
    public boolean doDeleteUserById(Integer id) {
        User user = this.userDAO.dbGetUserById(id);
        if(user.getRoles().stream().map(r -> r.getName().equals("ROLE_ADMIN")).toList().contains(Boolean.TRUE)) {
            return false;
        }
        return this.userDAO.dbDeleteUser(user);
    }

    @Override
    public boolean doUpdateTokenIp(Integer id,String token) {
        User user = this.userDAO.dbGetUserById(id);
        user.setTokenIp(token);
        this.userDAO.dbUpdateUser(user);
        return true;
    }

    private void udpateAuthentication(User currentUser,UserDTO updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(currentUser.getId() == updateDto.getId()) {
            UserDetails updatedUserDetails = new UserDTO(updateDto.getId(),
                    updateDto.getUsername(), updateDto.getPassword(), updateDto.getRoles());
            // Create a new Authentication object with the updated UserDetails
            Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                    updatedUserDetails, authentication.getCredentials(), authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
        }
    }

}
