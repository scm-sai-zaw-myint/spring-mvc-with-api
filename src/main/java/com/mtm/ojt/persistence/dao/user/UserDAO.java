package com.mtm.ojt.persistence.dao.user;

import java.util.List;

import com.mtm.ojt.persistence.entities.User;

public interface UserDAO {
    public List<User> dbGetUsers();
    public User dbAddUser(User u);
    public User dbUpdateUser(User u);
    public User getUserByName(String username);
    public User dbGetUserById(Integer id);
    public boolean dbDeleteUser(User user);
}
