package br.com.dbccompany.time7.gestaodeensino.dto.aluno;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlunoUpdateDTO {
    @Schema(description = "Nome do aluno")
    private String nome;

    @Schema(description = "Telefone do aluno")
    private String telefone;

    @Schema(description = "Identificador único do curso do aluno")
    private Integer idCurso;

    @Schema(description = "Identificador único do endereço do aluno")
    private Integer idEndereco;
}
