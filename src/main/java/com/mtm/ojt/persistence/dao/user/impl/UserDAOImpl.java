package com.mtm.ojt.persistence.dao.user.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mtm.ojt.persistence.dao.user.UserDAO;
import com.mtm.ojt.persistence.entities.User;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO{

    @Autowired
    SessionFactory factory;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<User> dbGetUsers() {
        Query query = factory.getCurrentSession().createQuery("FROM User u JOIN FETCH u.roles");
        return query.getResultList();
    }

    @Override
    public User dbUpdateUser(User u) {
        factory.getCurrentSession().update(u);
        return u;
    }

    @Override
    public User dbAddUser(User u) {
        factory.getCurrentSession().save(u);
        return u;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public User getUserByName(String username) {
        Query query = factory.getCurrentSession().createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :name");
        query.setParameter("name", username);
        return (User) query.getSingleResult();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public User dbGetUserById(Integer id) {
        Query query = factory.getCurrentSession().createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id=:id");
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

    @Override
    public boolean dbDeleteUser(User user) {
        factory.getCurrentSession().delete(user);
        return true;
    }

}
