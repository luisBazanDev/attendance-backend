package pe.bazan.luis.attendance.backend.v0;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.json.JSONArray;
import pe.bazan.luis.attendance.backend.service.JwtService;
import pe.bazan.luis.attendance.backend.v0.domain.requests.*;
import pe.bazan.luis.attendance.backend.v0.domain.response.*;
import pe.bazan.luis.attendance.backend.v0.dto.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

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

            StateGroup result = new GroupDTO().checkLimit(addPerson.getGroupId());

            if(result == StateGroup.AVAILABLE){

                Person groupResp = new PersonDTO().addPerson(addPerson);

                if (groupResp == null) {
                    return Response.ok(new JSONObject().put("message", "The person could not be created correctly").toString()).status(Response.Status.BAD_REQUEST).build();
                }

                return Response.ok(groupResp.toJSONObject().toString()).build();
            }

            return Response.ok(new JSONObject().put("message", "You have reached the limit of people in the group").toString()).status(Response.Status.TOO_MANY_REQUESTS).build();
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

    @POST
    @Path("/takeAttendace")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response takeAttendace(@Context ContainerRequestContext request, AttendanceReq attendanceReq) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER){
            AttendanceResp attendanceResp = new AttendanceDTO().take(attendanceReq);

            if (attendanceResp == null) {
                return Response.ok(new JSONObject().put("message", "The attendance could not be created correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(attendanceResp.toJSONObject().toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You are not an manager or admin to create session").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/getGroupStatistics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupStatistics(@Context ContainerRequestContext request, @QueryParam("groupId")String groupId) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER){
            GroupStatistics groupStatistics = new GroupDTO().getGroupStatistics(Integer.parseInt(groupId));

            if (groupStatistics == null) {
                return Response.ok(new JSONObject().put("message", "Results were not obtained correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(groupStatistics.toJSONObject().toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You don't have administrator role to use the api").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/showAllGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showAllGroups(@Context ContainerRequestContext request) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER){
            List<GroupResp> groupResps = new GroupDTO().showAllGroups();

            System.out.println(groupResps.size());

            if (groupResps == null) {
                return Response.ok(new JSONObject().put("message", "Results were not obtained correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            JSONArray jsonArray = new JSONArray();

            for (GroupResp groupResp : groupResps) {
                jsonArray.put(groupResp.toJSONObject());
            }

            return Response.ok(jsonArray.toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You don't have administrator role to use the api").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/searchGroupByName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchGroupByName(@Context ContainerRequestContext request, @QueryParam("name") String name) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER){
            GroupResp groupResps = new GroupDTO().SearchGroupByName(name);

            if (groupResps == null) {
                return Response.ok(new JSONObject().put("message", "Results were not obtained correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(groupResps.toJSONObject().toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You don't have administrator role to use the api").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/showAssignationByGroupID")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showAssignationByGroupID(@Context ContainerRequestContext request, @QueryParam("id") int groupId) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER){
            List<Assignation> groupResps = new GroupDTO().ShowAssignationByGroupID(groupId);

            System.out.println(groupResps.size());

            if (groupResps == null) {
                return Response.ok(new JSONObject().put("message", "Results were not obtained correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            JSONArray jsonArray = new JSONArray();

            for (Assignation groupResp : groupResps) {
                jsonArray.put(groupResp.toJSONObject());
            }

            return Response.ok(jsonArray.toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You don't have administrator role to use the api").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/searchSessionsByGroupID")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSessionsByGroupID(@Context ContainerRequestContext request, @QueryParam("id") int groupId) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER){
            List<SessionResp> groupResps = new SessionDTO().searchSessionsByGroupID(groupId);

            System.out.println(groupResps.size());

            if (groupResps == null) {
                return Response.ok(new JSONObject().put("message", "Results were not obtained correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            JSONArray jsonArray = new JSONArray();

            for (SessionResp groupResp : groupResps) {
                jsonArray.put(groupResp.toJSONObject());
            }

            return Response.ok(jsonArray.toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You don't have administrator role to use the api").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @GET
    @Path("/getSessionsToday")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSessionsToday(@Context ContainerRequestContext request) {
        User user = (User) request.getProperty("user");

        if(user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER){
            List<SessionResp> groupResps = new SessionDTO().getSessionsToday(user.getId());

            System.out.println(groupResps.size());

            if (groupResps == null) {
                return Response.ok(new JSONObject().put("message", "Results were not obtained correctly").toString()).status(Response.Status.BAD_REQUEST).build();
            }

            JSONArray jsonArray = new JSONArray();

            for (SessionResp groupResp : groupResps) {
                jsonArray.put(groupResp.toJSONObject());
            }

            return Response.ok(jsonArray.toString()).build();
        }

        return Response.status(401).entity(new JSONObject().put("message", "You don't have administrator role to use the api").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
