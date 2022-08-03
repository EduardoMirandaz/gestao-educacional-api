package br.com.dbccompany.time7.gestaodeensino.dto.disciplina;

import br.com.dbccompany.time7.gestaodeensino.dto.professor.ProfessorComposeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaDTO {

    @Schema(description = "ID exclusivo da disciplina")
    private Integer idDisciplina;;

    @Schema(description = "Nome da disciplina")
    private String nome;

    @Schema(description = "ID do professor da disciplina")
    private ProfessorComposeDTO professor;
}
