package nz.cloudns.jojo.controller;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import nz.cloudns.jojo.persistance.Task;
import nz.cloudns.jojo.persistance.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/tasks")
public class TaskResource {

  private static final Logger log = LoggerFactory.getLogger(TaskResource.class);
  @Inject
  SecurityIdentity securityIdentity;
  private final Integer DEFAULT_PAGE_NUM = 0;
  private final Integer MINIMUM_PAGE_SIZE = 1;
  private final Integer DEFAULT_PAGE_SIZE = 25;

  // Sample request
  // GET http://localhost:8000/tasks

  /**
   * Get the tasks which were created by the current user with pagination.
   *
   * @param page the page number of pagination.
   * @param size the page size of pagination.
   * @return the list of tasks of current page.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Task> listTasks(@QueryParam("page") int page, @QueryParam("size") int size) {
    DefaultJWTCallerPrincipal jwtPrincipal = (DefaultJWTCallerPrincipal) securityIdentity.getPrincipal();
    // Extract the email from the JWT token
    String email = jwtPrincipal.getClaim("email");
    System.out.println(securityIdentity.getPrincipal().getName());
    // Pagination
    // Set default values if no pagination parameters are provided
    int pageIndex = page > DEFAULT_PAGE_NUM ? page : DEFAULT_PAGE_NUM;
    int pageSize = size >= MINIMUM_PAGE_SIZE ? size : DEFAULT_PAGE_SIZE;

    // Create a paginated query
    PanacheQuery<Task> tasks = Task.findByEmail(email);
    System.out.println(tasks);
    tasks.page(Page.of(pageIndex, pageSize));
    return tasks.list();
  }

  // Sample request
  // GET http://localhost:8000/tasks/1

  /**
   * Get the specific task base on the given id.
   *
   * @param id the given id.
   * @return the task which id matches the given id.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Task getTaskById(@Nonnull @PathParam("id") Long id) {
    return Task.findById(id);
  }

  // Sample request PUT /tasks with below json body
  // "name": "task-3"

  /**
   * Add a task to the database.
   *
   * @param task the given task which will be added later.
   * @return the added task with id and user specified.
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public Task addTask(@Nonnull Task task) {
    // create date should be provided by server, not by client
    task.createDate = LocalDateTime.now(Clock.systemUTC());
    // new task should not be completed yet
    task.completed = false;
//    task.user = User.findByEmail(securityIdentity.getPrincipal().getName());
    task.persistAndFlush();
    return task;
  }

  // Sample request DELETE /tasks/1

  /**
   * Delete the task which id matches the given id.
   *
   * @param id the given id.
   */
  @DELETE
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public void deleteTask(@Nonnull @PathParam("id") Long id) {
    Task.deleteById(id);
  }

  /**
   * Update the task in the database which id matches the given task.
   *
   * @param task the given task which will be updated later.
   * @return the updated task.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Transactional
  public Task updateTask(@Nonnull Task task) {
    Task target = getTaskById(task.id);
    target.completed = task.completed;
    target.name = task.name;
    target.persistAndFlush();
    return target;
  }
}
