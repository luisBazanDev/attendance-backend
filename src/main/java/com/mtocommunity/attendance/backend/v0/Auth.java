package com.mtocommunity.attendance.backend.v0;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mtocommunity.attendance.backend.service.JwtService;
import com.mtocommunity.attendance.backend.v0.domain.requests.Login;
import com.mtocommunity.attendance.backend.v0.domain.response.User;
import com.mtocommunity.attendance.backend.v0.dto.UserDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
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
            return Response.ok(new JSONObject().put("message", "Username or password invalid").toString()).status(Response.Status.UNAUTHORIZED).build();
        }

        String bcryptHashString = user.getPassword();

        BCrypt.Result result = BCrypt.verifyer().verify(login.getPassword().toCharArray(), bcryptHashString);

        if(!result.verified) {
            // Invalid password
            return Response.ok(new JSONObject().put("message", "Username or password invalid").toString()).status(Response.Status.UNAUTHORIZED).build();
        }

        String token = new JwtService().generateToken(user.getUsername());

        JSONObject response = new JSONObject()
                .put("token", token)
                .put("user", user.toJSONObject());
        return Response.ok(response.toString()).build();
    }

    @GET
    @Path("/valid")
    @Produces("application/json")
    public Response valid(ContainerRequestContext request) {
        User user = (User) request.getProperty("user");

        return Response.ok((new JSONObject().put("user", user.toJSONObject())).toString()).build();
    }
}
