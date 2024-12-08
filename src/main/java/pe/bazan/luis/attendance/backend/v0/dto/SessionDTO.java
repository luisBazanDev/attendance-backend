package pe.bazan.luis.attendance.backend.v0.dto;

import pe.bazan.luis.attendance.backend.singleton.DatabaseConnection;
import pe.bazan.luis.attendance.backend.v0.dao.SessionDao;
import pe.bazan.luis.attendance.backend.v0.domain.requests.SessionReq;
import pe.bazan.luis.attendance.backend.v0.domain.response.Person;
import pe.bazan.luis.attendance.backend.v0.domain.response.PersonState;
import pe.bazan.luis.attendance.backend.v0.domain.response.SessionResp;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SessionDTO implements SessionDao {
    @Override
    public SessionResp create(SessionReq sessionReq) {
        String query = "CALL CreateOrAssignSession(?, ?, ?, ?, ?)";
        SessionResp sessionResp = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setInt(1, sessionReq.getGroupId());
            statement.setInt(2, sessionReq.getSessionNumber());
            statement.setTimestamp(3, Timestamp.valueOf(sessionReq.getStartAt()));
            statement.setInt(4, sessionReq.getDuration());
            statement.setString(5, sessionReq.getDescription());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                sessionResp = new SessionResp();
                sessionResp.setSessionId(resultSet.getInt("session_id"));
                sessionResp.setGroupId(resultSet.getInt("group_id"));
                sessionResp.setsessionNumber(resultSet.getInt("session_number"));
                sessionResp.setCreatedAt(resultSet.getTimestamp("start_at"));
                sessionResp.setDuration(resultSet.getInt("duration_in_minutes"));
                sessionResp.setDescription(resultSet.getString("description"));
            }
            resultSet.close();
            statement.close();
            return sessionResp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    @Override
    public List<SessionResp> searchSessionsByGroupID(int groupId) {
        String query = "CALL SearchSessionsByGroupID(?)";
        List<SessionResp> sessionResp = new ArrayList<>();

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sessionResp.add(new SessionResp(
                        resultSet.getInt("SessionID"),
                        resultSet.getInt("GroupID"),
                        resultSet.getInt("SessionNumber"),
                        resultSet.getInt("DurationInMinutes"),
                        resultSet.getTimestamp("StartAt"),
                        resultSet.getString("SessionDescription")
                ));
            }
            resultSet.close();
            statement.close();
            return sessionResp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<SessionResp> getSessionsToday(int user_id){
        String query = "CALL getSessionsToday(?)";
        List<SessionResp> sessionResp = new ArrayList<>();

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setInt(1, user_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sessionResp.add(new SessionResp(
                        resultSet.getInt("id"),
                        resultSet.getInt("group_id"),
                        resultSet.getInt("session_number"),
                        resultSet.getInt("duration_in_minutes"),
                        resultSet.getTimestamp("start_at"),
                        resultSet.getString("description")
                ));
            }
            resultSet.close();
            statement.close();
            return sessionResp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
