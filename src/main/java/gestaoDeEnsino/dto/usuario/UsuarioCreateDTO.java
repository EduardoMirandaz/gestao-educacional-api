package gestaoDeEnsino.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsuarioCreateDTO {

    @NotNull
    @Schema(example = "admin")
    private String login;

    @NotNull
    @Schema(example = "123")
    private String senha;
}
