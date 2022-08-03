package br.com.dbccompany.time7.gestaodeensino.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UsuarioCreateDTO {

    @Schema(example = "admin@gmail.com")
    @NotNull
    @Email
    private String login;

    @Schema(example = "123")
    @NotNull
    private String senha;
}
