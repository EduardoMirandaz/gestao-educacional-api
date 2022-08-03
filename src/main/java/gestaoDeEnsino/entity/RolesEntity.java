package gestaoDeEnsino.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "roles")
@Getter
@Setter
public class RolesEntity implements GrantedAuthority {

    @Id
    @SequenceGenerator(name = "ROLES_SEQ", sequenceName = "seq_roles", allocationSize = 1)
    @GeneratedValue(generator = "ROLES_SEQ", strategy = GenerationType.SEQUENCE)
    @Column(name = "id_roles")
    private Integer idRoles;

    @Column(name = "roles")
    private String roles;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "id_roles"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private Set<UsuarioEntity> usuarioEntities;

    @Override
    public String getAuthority() {
        return roles;
    }
}
