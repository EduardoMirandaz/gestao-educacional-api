package gestaoDeEnsino.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioUpdateDTO {

    @Schema(example = "admin")
    private String login;

    @Schema(example = "123")
    private String senha;
}
