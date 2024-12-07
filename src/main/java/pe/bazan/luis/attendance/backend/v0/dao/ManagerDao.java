package pe.bazan.luis.attendance.backend.v0.dao;

import pe.bazan.luis.attendance.backend.v0.domain.requests.ManagerReq;
import pe.bazan.luis.attendance.backend.v0.domain.response.ManagerResp;

public interface ManagerDao {
    public ManagerResp create(ManagerReq managerReq);
}
