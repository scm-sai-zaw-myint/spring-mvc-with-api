package com.mtm.ojt.bl.services.role.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mtm.ojt.bl.services.dto.RoleDTO;
import com.mtm.ojt.bl.services.role.RoleService;
import com.mtm.ojt.persistence.dao.role.RoleDAO;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;
    
    @Override
    public List<RoleDTO> getRoles() {
        return roleDAO.getRole().stream().map(r -> new RoleDTO(r) ).toList();
    }

}
