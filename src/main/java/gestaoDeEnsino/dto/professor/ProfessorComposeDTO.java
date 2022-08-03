package gestaoDeEnsino.dto.professor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorComposeDTO {

    @Schema(description = "ID do professor")
    private Integer idProfessor;

    @Schema(description = "Nome do professor")
    private String nome;

    @Schema(description = "Telefone do professor")
    private String telefone;

    @Schema(description = "E-mail do professor")
    private String email;
}
