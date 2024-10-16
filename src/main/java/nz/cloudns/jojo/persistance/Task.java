package nz.cloudns.jojo.persistance;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
public class Task extends PanacheEntity {

    @Nonnull
    @Column(name = "name")
    public String name;

    @Nonnull
    @Column(name = "completed")
    public boolean completed;

    @Column(name = "create_date")
    public LocalDateTime createDate;

    @Column(name = "email")
    public String email;

    public static PanacheQuery<Task> findByEmail(String email) {
        return find("email", Sort.by("createDate"), email);
    }
}
