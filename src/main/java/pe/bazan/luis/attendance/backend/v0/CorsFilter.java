package pe.bazan.luis.attendance.backend.v0;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if(isPreflightRequest(requestContext)) {
            requestContext.abortWith(Response.ok().build());
            return;
        }
    }

    private static boolean isPreflightRequest(ContainerRequestContext requestContext) {
        return requestContext.getHeaderString("Origin") != null && requestContext.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if(requestContext.getHeaderString("Origin") == null) {
            return;
        }

        if(isPreflightRequest(requestContext)) {
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        }

        responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
    }
}