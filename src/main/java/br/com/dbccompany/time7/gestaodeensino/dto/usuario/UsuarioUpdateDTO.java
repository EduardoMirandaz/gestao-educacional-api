package br.com.dbccompany.time7.gestaodeensino.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UsuarioUpdateDTO {

    @Schema(example = "admin@gmail.com")
    @Email
    private String login;

    @Schema(example = "123")
    private String senha;
}
