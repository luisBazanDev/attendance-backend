package pe.bazan.luis.attendance.backend.v0.dto;

import pe.bazan.luis.attendance.backend.singleton.DatabaseConnection;
import pe.bazan.luis.attendance.backend.v0.dao.GroupDao;
import pe.bazan.luis.attendance.backend.v0.domain.requests.GroupReq;
import pe.bazan.luis.attendance.backend.v0.domain.requests.StateGroup;
import pe.bazan.luis.attendance.backend.v0.domain.response.GroupResp;
import pe.bazan.luis.attendance.backend.v0.domain.response.User;
import pe.bazan.luis.attendance.backend.v0.domain.response.UserRole;

import java.sql.CallableStatement;
import java.sql.ResultSet;

public class GroupDTO implements GroupDao {
    @Override
    public GroupResp create(GroupReq data){
        String query = "CALL CreateGroup(?, ?, ?, ?)";
        GroupResp groupResp = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setString(1, data.getName());
            statement.setString(2, data.getDescription());
            statement.setInt(3, data.getAmount_persons());
            statement.setInt(4, data.getAmount_sessions());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                groupResp = new GroupResp();
                groupResp.setId(resultSet.getInt("id"));
                groupResp.setDescription(resultSet.getString("description"));
                groupResp.setName(resultSet.getString("name"));
                groupResp.setAmount_persons(resultSet.getInt("amount_persons"));
                groupResp.setAmount_sessions(resultSet.getInt("amount_sessions"));
            }
            resultSet.close();
            statement.close();
            return groupResp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public StateGroup checkLimit(int groupId){
        String query = "CALL CheckGroupLimit(?)";

        StateGroup stateGroup = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                stateGroup = StateGroup.valueOf(resultSet.getString("group_status"));
            }
            resultSet.close();
            statement.close();
            return stateGroup;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
