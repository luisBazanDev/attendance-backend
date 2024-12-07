package pe.bazan.luis.attendance.backend.v0.domain.requests;

public class ManagerReq {
    private String username, password, htop_seed, code_person, name, last_name, email, phone_number;
    private int group_id;

    public ManagerReq() {
    }

    public ManagerReq(String username, String password, String htop_seed, String code_person, String name, String last_name, String email, String phone_number, int group_id) {
        this.username = username;
        this.password = password;
        this.htop_seed = htop_seed;
        this.code_person = code_person;
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.group_id = group_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHtop_seed() {
        return htop_seed;
    }

    public void setHtop_seed(String htop_seed) {
        this.htop_seed = htop_seed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode_person() {
        return code_person;
    }

    public void setCode_person(String code_person) {
        this.code_person = code_person;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
