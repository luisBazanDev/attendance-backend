package pe.bazan.luis.attendance.backend.v0.domain.response;

import org.json.JSONObject;

import java.sql.Timestamp;

public class SessionResp {
    private int sessionId, groupId, sessionNumber;
    private Timestamp createdAt;
    private int duration;
    private String description;

    public SessionResp(int sessionId, int groupId, int sessionNumber, int duration, Timestamp createdAt, String description) {
        this.sessionId = sessionId;
        this.groupId = groupId;
        this.sessionNumber = sessionNumber;
        this.duration = duration;
        this.createdAt = createdAt;
        this.description = description;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getsessionNumber() {
        return sessionNumber;
    }

    public void setsessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SessionResp() {
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("sessionId", sessionId)
                .put("groupId", groupId)
                .put("sessionNumber", sessionNumber)
                .put("createdAt", createdAt)
                .put("duration", duration)
                .put("description", description);
    }
}
