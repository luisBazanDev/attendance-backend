package pe.bazan.luis.attendance.backend.v0.dto;

import pe.bazan.luis.attendance.backend.singleton.DatabaseConnection;
import pe.bazan.luis.attendance.backend.v0.dao.GroupDao;
import pe.bazan.luis.attendance.backend.v0.domain.requests.GroupReq;
import pe.bazan.luis.attendance.backend.v0.domain.requests.StateGroup;
import pe.bazan.luis.attendance.backend.v0.domain.response.*;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public GroupStatistics getGroupStatistics(int groupId){
        String query = "CALL GetGroupStatistics(?)";
        GroupStatistics groupStatistics = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                groupStatistics = new GroupStatistics();
                groupStatistics.setTotalPersons(resultSet.getInt("TotalPersons"));
                groupStatistics.setTotalSessions(resultSet.getInt("TotalSessions"));
                groupStatistics.setTotalAttendances(resultSet.getInt("TotalAttendance"));
            }
            resultSet.close();
            statement.close();
            return groupStatistics;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<GroupResp> showAllGroups(){
        String query = "CALL ShowAllGroups()";
        List<GroupResp> groupResps = new ArrayList<>();

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                groupResps.add(new GroupResp(
                        resultSet.getInt("GroupID"),
                        resultSet.getString("GroupName"),
                        resultSet.getString("GroupDescription"),
                        resultSet.getInt("TotalPersons"),
                        resultSet.getInt("TotalSessions")
                ));
            }
            resultSet.close();
            statement.close();
            return groupResps;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public GroupResp SearchGroupByName(String GroupId){
        String query = "CALL SearchGroupByName(?)";
        GroupResp groupResps = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setString(1, GroupId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                groupResps = new GroupResp();
                groupResps.setId(resultSet.getInt("GroupID"));
                groupResps.setDescription(resultSet.getString("GroupName"));
                groupResps.setName(resultSet.getString("GroupDescription"));
                groupResps.setAmount_persons(resultSet.getInt("TotalPersons"));
                groupResps.setAmount_sessions(resultSet.getInt("TotalSessions"));
            }
            resultSet.close();
            statement.close();
            return groupResps;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Assignation> ShowAssignationByGroupID(int groupID){
        String query = "CALL ShowAssignationByGroupID(?)";
        List<Assignation> assignations = new ArrayList<>();

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setInt(1, groupID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("AssignationID"));
                assignations.add(
                  new Assignation(
                          resultSet.getInt("AssignationID"),
                          PersonState.valueOf(resultSet.getString("PersonStatus")),
                          resultSet.getString("PersonPhoneNumber"),
                          resultSet.getString("PersonEmail"),
                          resultSet.getString("PersonLastName"),
                          resultSet.getString("PersonName"),
                          resultSet.getString("PersonCode")
                  )
                );
            }
            resultSet.close();
            statement.close();
            return assignations;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
