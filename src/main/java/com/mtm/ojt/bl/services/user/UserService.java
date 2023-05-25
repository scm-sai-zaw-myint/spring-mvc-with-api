package com.mtm.ojt.bl.services.user;

import java.util.List;

import com.mtm.ojt.bl.services.dto.UserDTO;
import com.mtm.ojt.web.forms.UserForm;

public interface UserService {
    
    public List<UserDTO> doGetUserList();
    
    public UserDTO doGetUserById(Integer id);
    
    public UserDTO doAddUser(UserForm form);
    
    public UserDTO doUpdateUser(UserForm form);
    
    public UserDTO doGetUserByName(String name);
    
    public boolean doDeleteUserById(Integer id);
    
    public boolean doUpdateTokenIp(Integer id,String token);

}
