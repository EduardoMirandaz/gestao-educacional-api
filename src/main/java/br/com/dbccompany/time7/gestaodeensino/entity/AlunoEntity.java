package br.com.dbccompany.time7.gestaodeensino.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Interceptor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "aluno")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class AlunoEntity extends PessoaEntity{

    @Id
    @SequenceGenerator(name = "seq_aluno2", sequenceName = "seq_aluno", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aluno2")
    @Column(name = "id_aluno")
    private Integer idAluno;

    @Column(name = "matricula")
    private Integer matricula;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuarioEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_curso", referencedColumnName = "id_curso")
    private CursoEntity cursoEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_endereco", referencedColumnName = "id_endereco")
    private EnderecoEntity enderecoEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "alunoEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE,
            orphanRemoval = true)
    private Set<NotaEntity> notaEntities;
}
