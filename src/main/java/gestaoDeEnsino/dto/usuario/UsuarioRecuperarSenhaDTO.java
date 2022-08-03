package gestaoDeEnsino.dto.usuario;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsuarioRecuperarSenhaDTO {

    @NotNull
    private String senha;
}
