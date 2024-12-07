package pe.bazan.luis.attendance.backend.v0.domain.requests;

import java.sql.Timestamp;

public class SessionReq {
    private int groupId;
    private int sessionNumber;
    private String startAt;
    private int duration;
    private String description;

    public SessionReq(int groupId, int sessionNumber, String startAt, int duration, String description) {
        this.groupId = groupId;
        this.sessionNumber = sessionNumber;
        this.startAt = startAt;
        this.duration = duration;
        this.description = description;
    }

    public SessionReq() {
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
