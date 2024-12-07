package pe.bazan.luis.attendance.backend.v0.dao;

import pe.bazan.luis.attendance.backend.v0.domain.requests.GroupReq;
import pe.bazan.luis.attendance.backend.v0.domain.requests.StateGroup;
import pe.bazan.luis.attendance.backend.v0.domain.response.Assignation;
import pe.bazan.luis.attendance.backend.v0.domain.response.GroupResp;
import pe.bazan.luis.attendance.backend.v0.domain.response.GroupStatistics;

import java.util.List;

public interface GroupDao {
    public GroupResp create(GroupReq group);
    public StateGroup checkLimit(int groupId);
    public GroupStatistics getGroupStatistics(int groupId);
    public List<GroupResp> showAllGroups();
    public GroupResp SearchGroupByName(String GroupId);
    public List<Assignation> ShowAssignationByGroupID(int groupID);
}
