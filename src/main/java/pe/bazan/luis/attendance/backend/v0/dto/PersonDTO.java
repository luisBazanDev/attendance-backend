package pe.bazan.luis.attendance.backend.v0.dto;

import pe.bazan.luis.attendance.backend.singleton.DatabaseConnection;
import pe.bazan.luis.attendance.backend.v0.dao.PersonDao;
import pe.bazan.luis.attendance.backend.v0.domain.requests.AddPerson;
import pe.bazan.luis.attendance.backend.v0.domain.response.Person;
import pe.bazan.luis.attendance.backend.v0.domain.response.PersonState;
import pe.bazan.luis.attendance.backend.v0.domain.response.User;
import pe.bazan.luis.attendance.backend.v0.domain.response.UserRole;

import javax.swing.plaf.nimbus.State;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public class PersonDTO implements PersonDao {
    @Override
    public Person addPerson(AddPerson person){
        String query = "CALL AddPersonToGroup(?, ?, ?, ?, ?, ?)";
        Person user = null;

        try {
            DatabaseConnection databaseInstance = DatabaseConnection.getInstancia();
            CallableStatement statement = databaseInstance.getConexion().prepareCall(query);
            statement.setString(1, person.getPersonId());
            statement.setString(2, person.getFirstName());
            statement.setString(3, person.getLastName());
            statement.setString(4, person.getPersonId() + "@utp.edu.pe");
            statement.setString(5, person.getPhone());
            statement.setInt(6, person.getGroupId());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new Person();
                user.setCode(resultSet.getString("code"));
                user.setFirstName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone_number"));
                user.setState(PersonState.valueOf(resultSet.getString("state")));
            }
            resultSet.close();
            statement.close();
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
