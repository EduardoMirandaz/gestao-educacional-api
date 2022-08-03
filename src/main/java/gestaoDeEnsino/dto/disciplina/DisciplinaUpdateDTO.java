package gestaoDeEnsino.dto.disciplina;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaUpdateDTO {
    @Schema(description = "Nome da disciplina")
    private String nome;

    @Schema(description = "ID do professor da disciplina")
    private Integer idProfessor;
}
