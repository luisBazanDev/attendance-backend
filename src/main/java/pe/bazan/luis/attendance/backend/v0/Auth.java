package pe.bazan.luis.attendance.backend.v0;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import pe.bazan.luis.attendance.backend.service.JwtService;
import pe.bazan.luis.attendance.backend.v0.domain.requests.AddPerson;
import pe.bazan.luis.attendance.backend.v0.domain.requests.GroupReq;
import pe.bazan.luis.attendance.backend.v0.domain.requests.Login;
import pe.bazan.luis.attendance.backend.v0.domain.response.GroupResp;
import pe.bazan.luis.attendance.backend.v0.domain.response.Person;
import pe.bazan.luis.attendance.backend.v0.domain.response.User;
import pe.bazan.luis.attendance.backend.v0.domain.response.UserRole;
import pe.bazan.luis.attendance.backend.v0.dto.GroupDTO;
import pe.bazan.luis.attendance.backend.v0.dto.PersonDTO;
import pe.bazan.luis.attendance.backend.v0.dto.UserDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
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

    @POST
    @Path("/createGroup")
    @Produces("application/json")
    public Response createGroup(ContainerRequestContext request, GroupReq groupReq) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN){
            GroupResp groupResp = new GroupDTO().create(groupReq);

            if (groupResp == null) {
                return Response.ok(new JSONObject().put("message", "The group could not be created correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(groupResp.toJSONObject().toString()).build();
        }
        return Response.status(401).entity(new JSONObject().put("message", "You are not an administrator to create groups").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @POST
    @Path("/addPersonToGroup")
    @Produces("application/json")
    public Response addPersonToGroup(ContainerRequestContext request, AddPerson addPerson) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER){
            Person groupResp = new PersonDTO().addPerson(addPerson);

            if (groupResp == null) {
                return Response.ok(new JSONObject().put("message", "The person could not be created correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(groupResp.toJSONObject().toString()).build();
        }
        return Response.status(401).entity(new JSONObject().put("message", "You are not an administrator and manager to create person").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }


}
