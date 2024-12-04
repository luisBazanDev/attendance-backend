package pe.bazan.luis.attendance.backend.v0.dao;

import pe.bazan.luis.attendance.backend.v0.domain.response.User;

public interface UserDao {
    public User findUserByUsername(String username);
}
