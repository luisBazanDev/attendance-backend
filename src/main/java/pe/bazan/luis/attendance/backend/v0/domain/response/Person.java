package pe.bazan.luis.attendance.backend.v0.domain.response;

import org.json.JSONObject;

public class Person {
    private String code, firstName, lastName, email, phone;
    private PersonState state;

    public Person() {
    }

    public Person(String code, String firstName, String lastName, String email, String phone, PersonState state) {
        this.code = code;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PersonState getState() {
        return state;
    }

    public void setState(PersonState state) {
        this.state = state;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("code", code)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("email", email)
                .put("phone", phone)
                .put("state", state);
    }
}
