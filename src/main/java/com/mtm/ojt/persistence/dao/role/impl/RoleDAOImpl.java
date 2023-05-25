package com.mtm.ojt.persistence.dao.role.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mtm.ojt.persistence.dao.role.RoleDAO;
import com.mtm.ojt.persistence.entities.Role;

@Repository
@Transactional
public class RoleDAOImpl implements RoleDAO {

    @Autowired
    SessionFactory sessionFactory;
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    public List<Role> getRole() {
        Query query = sessionFactory.getCurrentSession().createQuery("FROM Role u");
        return query.list();
    }

    @Override
    public Role createRole(Role role) {
        sessionFactory.getCurrentSession().save(role);
        return role;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Role getRoleById(Integer id) {
        Query query = sessionFactory.getCurrentSession().createQuery("SELECT r FROM Role r WHERE r.id = :id");
        query.setParameter("id", id);
        return (Role) query.getSingleResult();
    }

}
