package org.example.user.repository;

import org.example.user.model.User;

public interface UserDao {

    void persist(User user);
}
