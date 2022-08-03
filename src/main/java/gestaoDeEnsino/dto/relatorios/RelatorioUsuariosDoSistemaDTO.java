package gestaoDeEnsino.dto.relatorios;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioUsuariosDoSistemaDTO {

    @Schema(description = "ID do usuario")
    private Integer idUsuario;

    @Schema(description = "Nome do usuario")
    private String nome;

    @Schema(description = "Nome de usuário utilizado pela pessoa no sistema")
    private String nomeUsuario;

    @Schema(description = "Status do usuário no sistema")
    private Boolean status;


}
