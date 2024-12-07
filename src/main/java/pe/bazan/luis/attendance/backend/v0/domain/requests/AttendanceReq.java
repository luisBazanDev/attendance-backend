package pe.bazan.luis.attendance.backend.v0.domain.requests;

public class AttendanceReq {
    private String personId;
    private int groupId;
    private int session_number;

    public AttendanceReq(String personId, int groupId, int session_number) {
        this.personId = personId;
        this.groupId = groupId;
        this.session_number = session_number;
    }

    public AttendanceReq() {
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSession_number() {
        return session_number;
    }

    public void setSession_number(int session_number) {
        this.session_number = session_number;
    }
}
