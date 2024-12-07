package pe.bazan.luis.attendance.backend.v0.dto;

import pe.bazan.luis.attendance.backend.singleton.DatabaseConnection;
import pe.bazan.luis.attendance.backend.v0.dao.ManagerDao;
import pe.bazan.luis.attendance.backend.v0.domain.requests.ManagerReq;
import pe.bazan.luis.attendance.backend.v0.domain.response.GroupResp;
import pe.bazan.luis.attendance.backend.v0.domain.response.ManagerResp;
import pe.bazan.luis.attendance.backend.v0.domain.response.UserRole;

import java.sql.CallableStatement;
import java.sql.ResultSet;

public class ManagerDTO implements ManagerDao {
    @Override
    public ManagerResp create(ManagerReq managerReq) {
        String query = "CALL AddManagerToGroup(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        ManagerResp managerResp = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setString(1, managerReq.getUsername());
            statement.setString(2, managerReq.getPassword());
            statement.setString(3, managerReq.getHtop_seed());
            statement.setString(4, managerReq.getCode_person());
            statement.setString(5, managerReq.getName());
            statement.setString(6, managerReq.getLast_name());
            statement.setString(7, managerReq.getEmail());
            statement.setString(8, managerReq.getPhone_number());
            statement.setInt(9, managerReq.getGroup_id());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                managerResp = new ManagerResp();
                managerResp.setUser_id(resultSet.getInt("user_id"));
                managerResp.setUsername(resultSet.getString("username"));
                managerResp.setCode_person(resultSet.getString("code_person"));
                managerResp.setRole(UserRole.valueOf(resultSet.getString("role")));
                managerResp.setName(resultSet.getString("name"));
                managerResp.setLast_name(resultSet.getString("last_name"));
                managerResp.setEmail(resultSet.getString("email"));
            }
            resultSet.close();
            statement.close();
            return managerResp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
