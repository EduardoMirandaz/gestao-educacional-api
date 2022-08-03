package br.com.dbccompany.time7.gestaodeensino.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Entity(name = "notas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class NotaEntity {

    @Id
    @SequenceGenerator(name = "NOTAS_SEQ", sequenceName = "seq_notas", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTAS_SEQ")
    @Column(name = "id_notas")
    private Integer idNota;

    @Column(name = "n1")
    private Double nota1;

    @Column(name = "n2")
    private Double nota2;

    @Column(name = "n3")
    private Double nota3;

    @Column(name = "n4")
    private Double nota4;

    @Column(name = "media")
    private Double media;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_disciplina", referencedColumnName = "id_disciplina")
    private DisciplinaEntity disciplinaEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_aluno", referencedColumnName = "id_aluno")
    private AlunoEntity alunoEntity;
}
