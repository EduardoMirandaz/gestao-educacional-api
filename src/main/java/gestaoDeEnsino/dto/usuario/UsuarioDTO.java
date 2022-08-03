package gestaoDeEnsino.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioDTO {

    @Schema(description = "Id do Usuario")
    private Integer idUsuario;

    @Schema(description = "Login do Usuario")
    private String login;
}
