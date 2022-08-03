package br.com.dbccompany.time7.gestaodeensino.dto.disciplina;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaCreateDTO {

    @Schema(description = "Nome da disciplina")
    @NotBlank(message = "Insira o nome da disciplina")
    private String nome;

    @Schema(description = "ID do professor da disciplina")
    private Integer idProfessor;
}
