package pe.bazan.luis.attendance.backend.v0;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import org.jboss.weld.environment.se.bindings.Parameters;
import pe.bazan.luis.attendance.backend.service.JwtService;
import pe.bazan.luis.attendance.backend.v0.domain.requests.*;
import pe.bazan.luis.attendance.backend.v0.domain.response.*;
import pe.bazan.luis.attendance.backend.v0.dto.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

@Path("/auth")
public class Auth {
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response valid(@Context ContainerRequestContext request) {
        User user = (User) request.getProperty("user");

        return Response.ok((new JSONObject().put("user", user.toJSONObject())).toString()).build();
    }

    @POST
    @Path("/createGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGroup(@Context ContainerRequestContext request, GroupReq groupReq) {
        User user = (User) request.getProperty("user");

        System.out.println(groupReq.getName());

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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPersonToGroup(@Context ContainerRequestContext request, AddPerson addPerson) {
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

    @POST
    @Path("/addManager")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addManager(@Context ContainerRequestContext request, ManagerReq managerReq) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN){

            String result = BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(12, managerReq.getPassword().toCharArray());

            managerReq.setPassword(result);

            ManagerResp managerResp = new ManagerDTO().create(managerReq);

            if (managerResp == null) {
                return Response.ok(new JSONObject().put("message", "The manager could not be created correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(managerResp.toJSONObject().toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You are not an administrator to create manager").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @POST
    @Path("/createSession")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSession(@Context ContainerRequestContext request, SessionReq sessionReq) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN){
            SessionResp sessionResp = new SessionDTO().create(sessionReq);

            if (sessionResp == null) {
                return Response.ok(new JSONObject().put("message", "The session could not be created correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(sessionResp.toJSONObject().toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You are not an administrator to create session").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
