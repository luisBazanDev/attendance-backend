package pe.bazan.luis.attendance.backend.v0.dto;

import pe.bazan.luis.attendance.backend.singleton.DatabaseConnection;
import pe.bazan.luis.attendance.backend.v0.dao.AttendanceDao;
import pe.bazan.luis.attendance.backend.v0.domain.requests.AttendanceReq;
import pe.bazan.luis.attendance.backend.v0.domain.requests.StateGroup;
import pe.bazan.luis.attendance.backend.v0.domain.response.AttendanceResp;

import java.sql.CallableStatement;
import java.sql.ResultSet;

public class AttendanceDTO implements AttendanceDao {
    @Override
    public AttendanceResp take(AttendanceReq attendanceReq) {
        String query = "CALL TakeAttendance(?, ?, ?)";

        AttendanceResp attendanceResp = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setString(1, attendanceReq.getPersonId());
            statement.setInt(2, attendanceReq.getGroupId());
            statement.setInt(3, attendanceReq.getSession_number());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                attendanceResp = new AttendanceResp();
                attendanceResp.setResultMessage(resultSet.getString("ResultMessage"));
            }
            resultSet.close();
            statement.close();
            return attendanceResp;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
