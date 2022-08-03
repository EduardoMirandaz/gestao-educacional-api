package br.com.dbccompany.time7.gestaodeensino.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Entity(name = "curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoEntity {

    @Id
    @SequenceGenerator(name = "seq_curso2", sequenceName = "seq_curso", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_curso2")
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "nome")
    private String nome;

    @JsonIgnore
    @OneToMany(mappedBy = "cursoEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    private Set<AlunoEntity> alunosEntities;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "disciplina_x_curso",
            joinColumns = @JoinColumn(name = "id_curso"),
            inverseJoinColumns = @JoinColumn(name = "id_disciplina"))
    private Set<DisciplinaEntity> disciplinasEntities;
}
