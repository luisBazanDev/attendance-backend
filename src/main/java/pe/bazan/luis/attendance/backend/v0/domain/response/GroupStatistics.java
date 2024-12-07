package pe.bazan.luis.attendance.backend.v0.domain.response;

import org.json.JSONObject;

public class GroupStatistics {
    private int TotalPersons, TotalSessions, TotalAttendances;

    public GroupStatistics() {
    }

    public GroupStatistics(int totalPersons, int totalSessions, int totalAttendances) {
        TotalPersons = totalPersons;
        TotalSessions = totalSessions;
        TotalAttendances = totalAttendances;
    }

    public int getTotalPersons() {
        return TotalPersons;
    }

    public void setTotalPersons(int totalPersons) {
        TotalPersons = totalPersons;
    }

    public int getTotalSessions() {
        return TotalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        TotalSessions = totalSessions;
    }

    public int getTotalAttendances() {
        return TotalAttendances;
    }

    public void setTotalAttendances(int totalAttendances) {
        TotalAttendances = totalAttendances;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("TotalPersons", TotalPersons)
                .put("TotalSessions", TotalSessions)
                .put("TotalAttendances", TotalAttendances);
    }
}
