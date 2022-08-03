package gestaoDeEnsino.dto.curso;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoCreateDTO {

    @Schema(description = "Nome do curso")
    @NotBlank
    private String nome;
}
