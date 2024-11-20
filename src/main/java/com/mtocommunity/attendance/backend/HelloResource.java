package com.mtocommunity.attendance.backend;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

@Path("/hello-world")
public class HelloResource {
    @GET
    @Produces("application/json")
    public Response hello() {
        return Response.ok(new JSONObject().put("message", "Hello world from Tomcat!").toString()).build();
    }
}