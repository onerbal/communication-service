package org.example.user.service;

import org.example.user.model.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User findById(Long id);

    List<User> findAll();

    void generateMockUsers();
}
