package nz.cloudns.jojo.controller;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import java.util.List;

import nz.cloudns.jojo.persistance.User;


@Path("/sign-up")
public class SignUpResource {

  /**
   * Add a user to the database if the user is not exist.
   *
   * @param user the user being signed up, encoded as a User.
   * @return the created User if the user is added successfully, throw the UserAlreadyExists
   * Exception otherwise.
   */
  @PUT
  @Transactional
  public User add(User user) throws UserAlreadyExistsException {
    // Check whether the same email is already exist.
    if (User.userExist(user.email)) {
      throw new UserAlreadyExistsException();
    }
    User.add(user.email, user.password, "user");
    return User.findByEmail(user.email);
  }
}
