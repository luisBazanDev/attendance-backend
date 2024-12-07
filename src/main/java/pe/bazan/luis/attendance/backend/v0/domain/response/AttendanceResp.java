package pe.bazan.luis.attendance.backend.v0.domain.response;

import org.json.JSONObject;

public class AttendanceResp {
    private String resultMessage;

    public AttendanceResp(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public AttendanceResp() {
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("message", resultMessage);
    }
}
