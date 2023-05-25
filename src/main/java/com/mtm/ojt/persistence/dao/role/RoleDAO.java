package com.mtm.ojt.persistence.dao.role;

import java.util.List;

import com.mtm.ojt.persistence.entities.Role;

public interface RoleDAO {
    public List<Role> getRole();
    public Role createRole(Role role);
    public Role getRoleById(Integer Id);
}
