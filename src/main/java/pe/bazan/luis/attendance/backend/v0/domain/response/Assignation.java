package pe.bazan.luis.attendance.backend.v0.domain.response;

import org.json.JSONObject;

public class Assignation {
    private int AssignationID;
    private String PersonCode;
    private String PersonName;
    private String PersonLastName;
    private String PersonEmail;
    private String PersonPhone;
    private PersonState PersonState;

    public Assignation() {
    }

    public Assignation(int assignationID, PersonState personState, String personPhone, String personEmail, String personLastName, String personName, String personCode) {
        AssignationID = assignationID;
        PersonState = personState;
        PersonPhone = personPhone;
        PersonEmail = personEmail;
        PersonLastName = personLastName;
        PersonName = personName;
        PersonCode = personCode;
    }

    public int getAssignationID() {
        return AssignationID;
    }

    public void setAssignationID(int assignationID) {
        AssignationID = assignationID;
    }

    public String getPersonCode() {
        return PersonCode;
    }

    public void setPersonCode(String personCode) {
        PersonCode = personCode;
    }

    public String getPersonName() {
        return PersonName;
    }

    public void setPersonName(String personName) {
        PersonName = personName;
    }

    public String getPersonLastName() {
        return PersonLastName;
    }

    public void setPersonLastName(String personLastName) {
        PersonLastName = personLastName;
    }

    public String getPersonEmail() {
        return PersonEmail;
    }

    public void setPersonEmail(String personEmail) {
        PersonEmail = personEmail;
    }

    public String getPersonPhone() {
        return PersonPhone;
    }

    public void setPersonPhone(String personPhone) {
        PersonPhone = personPhone;
    }

    public PersonState getPersonState() {
        return PersonState;
    }

    public void setPersonState(PersonState personState) {
        PersonState = personState;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("AssignationID", AssignationID)
                .put("PersonCode", PersonCode)
                .put("PersonName", PersonName)
                .put("PersonLastName", PersonLastName)
                .put("PersonEmail", PersonEmail)
                .put("PersonPhone", PersonPhone)
                .put("PersonState", PersonState);
    }
}
