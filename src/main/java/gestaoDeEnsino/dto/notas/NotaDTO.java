package gestaoDeEnsino.dto.notas;

import gestaoDeEnsino.dto.disciplina.DisciplinaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTO {

    @Schema(description = "ID único que relaciona uma disciplina e um aluno")
    private Integer idNota;

    @Schema(description = "ID da disciplina relacionada à nota")
    private DisciplinaDTO disciplinaDTO;

    @Schema(description = "Nota 1 do aluno, referente à disciplina")
    private Double nota1;

    @Schema(description = "Nota 2 do aluno, referente à disciplina")
    private Double nota2;

    @Schema(description = "Nota 3 do aluno, referente à disciplina")
    private Double nota3;

    @Schema(description = "Nota 4 do aluno, referente à disciplina")
    private Double nota4;

    @Schema(description = "Media das notas do aluno, referente à disciplina")
    private Double media;
}
