package pe.bazan.luis.attendance.backend.v0.dao;

import pe.bazan.luis.attendance.backend.v0.domain.requests.AddPerson;
import pe.bazan.luis.attendance.backend.v0.domain.response.Person;

public interface PersonDao {
    public Person addPerson(AddPerson addPerson);
}
