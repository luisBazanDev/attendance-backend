package pe.bazan.luis.attendance.backend.v0.dao;

import pe.bazan.luis.attendance.backend.v0.domain.requests.SessionReq;
import pe.bazan.luis.attendance.backend.v0.domain.response.SessionResp;

public interface SessionDao {
    public SessionResp create(SessionReq session);
}
