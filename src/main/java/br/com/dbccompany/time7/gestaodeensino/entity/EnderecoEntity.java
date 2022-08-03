package br.com.dbccompany.time7.gestaodeensino.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.util.Set;
import javax.persistence.*;

@Entity(name = "endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoEntity {

    @Id
    @SequenceGenerator(name = "ENDERECO_SEQ", sequenceName = "seq_endereco", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENDERECO_SEQ")
    @Column(name = "id_endereco")
    private Integer idEndereco;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "estado")
    private String estado;

    @Column(name = "cep")
    private String cep;

    @Column(name = "complemento")
    private String complemento;

    @JsonIgnore
    @OneToMany(mappedBy = "enderecoEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    private Set<AlunoEntity> alunoEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "enderecoEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    private Set<ProfessorEntity> professorEntities;
}
