package pe.bazan.luis.attendance.backend.v0.domain.response;

import org.json.JSONObject;

public class GroupResp {
    private int Id;
    private String name, description;
    private int amount_persons, amount_sessions;

    public GroupResp() {
    }

    public GroupResp(int id, String name, String description, int amount_persons, int amount_sessions) {
        Id = id;
        this.name = name;
        this.description = description;
        this.amount_persons = amount_persons;
        this.amount_sessions = amount_sessions;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount_persons() {
        return amount_persons;
    }

    public void setAmount_persons(int amount_persons) {
        this.amount_persons = amount_persons;
    }

    public int getAmount_sessions() {
        return amount_sessions;
    }

    public void setAmount_sessions(int amount_sessions) {
        this.amount_sessions = amount_sessions;
    }

    public JSONObject toJSONObject() {
        return new JSONObject()
                .put("Id", Id)
                .put("name", name)
                .put("description", description)
                .put("amount_persons", amount_persons)
                .put("amount_sessions", amount_sessions);
    }
}
