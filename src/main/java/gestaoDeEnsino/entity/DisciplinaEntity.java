package gestaoDeEnsino.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Entity(name = "disciplina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaEntity {

    @Id
    @SequenceGenerator(name = "seq_disciplina2", sequenceName = "seq_disciplina", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_disciplina2")
    @Column(name = "id_disciplina")
    private Integer idDisciplina;

    @Column(name = "nome")
    private String nome;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_professor", referencedColumnName = "id_professor")
    private ProfessorEntity professorEntity;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "disciplina_x_curso",
            joinColumns = @JoinColumn(name = "id_disciplina"),
            inverseJoinColumns = @JoinColumn(name = "id_curso"))
    private Set<CursoEntity> cursosEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "disciplinaEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true)
    private Set<NotaEntity> notaEntities;
}
