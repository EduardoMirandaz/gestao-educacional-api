package br.com.dbccompany.time7.gestaodeensino.dto.aluno;

import br.com.dbccompany.time7.gestaodeensino.dto.curso.CursoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.endereco.EnderecoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {
    @Schema(description = "Identificador único do Aluno")
    private Integer idAluno;

    @Schema(description = "Nome do aluno")
    private String nome;

    @Schema(description = "Número de matrícula do aluno")
    private Integer matricula;

    @Schema(description = "Telefone do aluno")
    private String telefone;

    @Schema(description = "E-mail do aluno")
    private String email;

    @Schema(description = "Identificador único do curso do aluno")
    private CursoDTO curso;

    @Schema(description = "Identificador único do endereço do aluno")
    private EnderecoDTO endereco;
}
