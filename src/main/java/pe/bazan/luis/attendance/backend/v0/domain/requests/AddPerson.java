package pe.bazan.luis.attendance.backend.v0.domain.requests;

public class AddPerson {
    private String personId, firstName, lastName, email, phone;
    private int groupId;

    public AddPerson() {
    }

    public AddPerson(String personId, String phone, int groupId, String lastName, String email, String firstName) {
        this.personId = personId;
        this.phone = phone;
        this.groupId = groupId;
        this.lastName = lastName;
        this.email = email;
        this.firstName = firstName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
