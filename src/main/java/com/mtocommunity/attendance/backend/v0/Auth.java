package com.mtocommunity.attendance.backend.v0;

import com.mtocommunity.attendance.backend.v0.dao.UserDao;
import com.mtocommunity.attendance.backend.v0.domain.requests.Login;
import com.mtocommunity.attendance.backend.v0.domain.response.User;
import com.mtocommunity.attendance.backend.v0.dto.UserDTO;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

@Path("/auth")
public class Auth {
    @POST
    @Path("/")
    @Produces("application/json")
    public Response login(Login login) {
        User user = new UserDTO().findUserByUsername(login.getUsername());

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // TODO: VALID PASSWORD

        JSONObject response = new JSONObject()
                .put("token", "YOUR TOKEN")
                .put("user", user.toJSONObject());
        return Response.ok(response.toString()).build();
    }
}
