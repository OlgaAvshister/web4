package com.graphic.lab4.service;

import com.graphic.lab4.model.data.User;

public interface UserService {
    User save(User user);

    User findByUsername(String username);
}
