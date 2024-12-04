package pe.bazan.luis.attendance.backend.v0;

import pe.bazan.luis.attendance.backend.service.JwtService;
import pe.bazan.luis.attendance.backend.v0.domain.response.User;
import pe.bazan.luis.attendance.backend.v0.dto.UserDTO;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.json.JSONObject;

import java.io.IOException;

@Provider
public class JwtFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        String path = context.getUriInfo().getRequestUri().getPath();

        if (path.startsWith("/api")) {
            doFilterApiRoutes(context);
        }
    }

    private void doFilterApiRoutes(ContainerRequestContext context) {
        String[] routeParts = context.getUriInfo().getRequestUri().getPath().split("/");

        if (routeParts.length < 3) {
            abortRequest(context);
            return;
        }

        String[] restRouteParts = new String[routeParts.length - 3];
        System.arraycopy(routeParts, 3, restRouteParts, 0, restRouteParts.length);
        String restRoute = "/" + String.join("/", restRouteParts);

        String[] publicRouteParts = {"/auth"};

        for (String publicRoute : publicRouteParts) {
            if (publicRoute.equals(restRoute)) {
                return;
            }
        }

        doFilterPrivateRoutes(context, restRoute);
    }

    private void doFilterPrivateRoutes(ContainerRequestContext context, String restRoute) {
        String authorization = context.getHeaders().getFirst("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            abortRequest(context);
            return;
        }

        JwtService jwtService = new JwtService();

        String token = authorization.substring(7);

        if (!jwtService.isValid(token)) {
            abortRequest(context);
            return;
        }

        User user = new UserDTO().findUserByUsername(jwtService.extractSubject(token));
        context.setProperty("user", user);
    }

    private void abortRequest(ContainerRequestContext context) {
        Response response = Response.status(403).entity(new JSONObject().put("message", "Token expired or don't have token").toString()).type(MediaType.APPLICATION_JSON_TYPE).build();
        context.abortWith(response);
    }
}
