package com.mtocommunity.attendance.backend.v0.dto;

import com.mtocommunity.attendance.backend.singleton.DatabaseConnection;
import com.mtocommunity.attendance.backend.v0.dao.UserDao;
import com.mtocommunity.attendance.backend.v0.domain.response.User;
import com.mtocommunity.attendance.backend.v0.domain.response.UserRole;

import java.sql.CallableStatement;
import java.sql.ResultSet;

public class UserDTO implements UserDao {
    @Override
    public User findUserByUsername(String username) {
        String query = "CALL getUser(?)";
        User user = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setName(resultSet.getString("name"));
                user.setRole(UserRole.valueOf(resultSet.getString("role")));
                user.setPhone_number(resultSet.getString("phone_number"));
                user.setPassword(resultSet.getString("password"));
            }
            resultSet.close();
            statement.close();
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
