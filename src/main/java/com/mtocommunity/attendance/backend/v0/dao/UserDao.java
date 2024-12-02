package com.mtocommunity.attendance.backend.v0.dao;

import com.mtocommunity.attendance.backend.v0.domain.response.User;

public interface UserDao {
    public User findUserByUsername(String username);
}
