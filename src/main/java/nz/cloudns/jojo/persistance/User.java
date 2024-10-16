package nz.cloudns.jojo.persistance;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;

@Entity
@Table(name = "user_information")
@UserDefinition
public class User extends PanacheEntity {

  @Username
  public String email;
  @Password
  public String password;
  @Roles
  public String role;

  /**
   * Find a user base on the given email
   * @param email user email, encoded as a String
   * @return the User which email matches the given email
   */
  public static User findByEmail(String email) {
    return find("email", email).firstResult();
  }

  public static boolean userExist(String email) {
    return find("email", email).count() > 0;
  }

  /**
   * Adds a new user to the database
   * @param username, the username of the user
   * @param password, the password of the user
   * @param role, the role of the user
   */
  public static void add(String username, String password, String role) {
    User user = new User();
    user.email = username;
    user.password = BcryptUtil.bcryptHash(password);
    user.role = role;
    user.persistAndFlush();
  }
}
