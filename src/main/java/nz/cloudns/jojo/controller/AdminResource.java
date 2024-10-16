package nz.cloudns.jojo.controller;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/admin")
@Authenticated
public class AdminResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String admin() {
    return "granted";
  }
}
