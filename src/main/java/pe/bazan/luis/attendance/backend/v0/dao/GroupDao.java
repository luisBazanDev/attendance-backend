package pe.bazan.luis.attendance.backend.v0.dao;

import pe.bazan.luis.attendance.backend.v0.domain.requests.GroupReq;
import pe.bazan.luis.attendance.backend.v0.domain.requests.StateGroup;
import pe.bazan.luis.attendance.backend.v0.domain.response.GroupResp;

public interface GroupDao {
    public GroupResp create(GroupReq group);
    public StateGroup checkLimit(int groupId);
}
