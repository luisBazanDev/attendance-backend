package pe.bazan.luis.attendance.backend.v0.dao;

import pe.bazan.luis.attendance.backend.v0.domain.requests.AttendanceReq;
import pe.bazan.luis.attendance.backend.v0.domain.response.AttendanceResp;

public interface AttendanceDao {
    public AttendanceResp take(AttendanceReq restData );
}
