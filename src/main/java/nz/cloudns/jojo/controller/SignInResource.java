package nz.cloudns.jojo.controller;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import nz.cloudns.jojo.persistance.User;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@Path("/sign-in")
public class SignInResource {


  @POST
  public Response signIn(User user) {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      HttpPost post = new HttpPost(
          "http://localhost:52539/realms/quarkus/protocol/openid-connect/token");

      //Set headers
      post.setHeader("Content-Type", "application/x-www-form-urlencoded");

      // Set the request body
      StringEntity entity = new StringEntity(
          "client_id=backend&" +
              "client_secret=yOaenGUFhSi8sYpp5nlee72TaFd3B3ov&" +
              "grant_type=password&" +
              "username=" + user.email + "&" +
              "password=" + user.password
      );
      post.setEntity(entity);

      try(CloseableHttpResponse response = client.execute(post)) {
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONObject json = new JSONObject(responseBody);

        if (response.getStatusLine().getStatusCode() == 200) {
          String token = json.getString("access_token");
          return Response.ok().entity(token).build();
        } else {
          return Response.status(Status.UNAUTHORIZED).entity(json.toString()).build();
        }
      } catch (Exception e) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
    } catch (Exception e) {
      return Response.status(Status.UNAUTHORIZED).build();
    }
  }
}
