package pe.bazan.luis.attendance.backend.v0.domain.response;

import org.json.JSONObject;

public class ManagerResp {
    private int user_id;
        private String username, code_person;
        private UserRole role;
        private String name, last_name, email;

    public ManagerResp() {
    }

    public ManagerResp(int user_id, String username, String code_person, UserRole role, String name, String last_name, String email) {
        this.user_id = user_id;
        this.username = username;
        this.code_person = code_person;
        this.role = role;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode_person() {
        return code_person;
    }

    public void setCode_person(String code_person) {
        this.code_person = code_person;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("user_id", user_id)
                .put("username", username)
                .put("code_person", code_person)
                .put("role", role)
                .put("name", name)
                .put("last_name", last_name)
                .put("email", email);
    }
}
