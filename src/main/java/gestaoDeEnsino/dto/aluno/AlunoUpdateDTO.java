package gestaoDeEnsino.dto.aluno;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlunoUpdateDTO {
    @Schema(description = "Nome do aluno")
    private String nome;

    @Schema(description = "Telefone do aluno")
    private String telefone;

    @Schema(description = "E-mail do aluno")
    private String email;

    @Schema(description = "Identificador único do curso do aluno")
    private Integer idCurso;

    @Schema(description = "Identificador único do endereço do aluno")
    private Integer idEndereco;
}
